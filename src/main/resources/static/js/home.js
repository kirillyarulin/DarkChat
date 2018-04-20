let currentChat;
let chatHistory = document.getElementById("js-chat-history");
let messageInput = document.getElementById("js-message-input");
let stompClient;

// let header;
// let token;

//Объекты allUsers, userChats и currentUser создаются на стороне сервера


window.onload = function (ev) {

    //Сортировка чатов
    userChats = userChats.sort(function (a, b) {
        let time1 = a.timeOfCreation;
        let t = getLastChatMessage(a);
        if (t !== undefined) {
            time1 = t.time;
        }

        let time2 = b.timeOfCreation;
        t = getLastChatMessage(b);
        if (t !== undefined) {
            time2 = t.time;
        }

        return Date.parse(time2) - Date.parse(time1);
    });


    //Построение списка чатов
    userChats.forEach(function (chat) {
        showChat(chat);
    });


    //Обработка отправки сообщений с помощью WebSocket
    let socket = new SockJS('/chat-messaging');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/chat/messages', function (message) {
            chatMessage = JSON.parse(message.body);
            userChats.forEach(function (chat) {
                if (chatMessage.chat.chatId === chat.chatId) {
                    if (chat.messages === undefined) {
                        chat.messages = getChatMessages(chat);
                    } else {
                        chat.messages.push(chatMessage);
                    }
                    moveChatUp(chat);
                    showLastMessage(chat, chatMessage);
                }
            });

            if (currentChat !== undefined && currentChat.chatId === chatMessage.chat.chatId) {
                showMessage(chatMessage);
                moveChatHistory();
            }
        });

    });

    document.getElementById("js-header-exit").addEventListener("click", function (evt) {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
    });


    //Обработка закрытия чата
    document.getElementById("js-chat-body-close").addEventListener("click", closeChat);

    //Обработка движения поля ввода при вводе многострочных сообщений
    messageInput.addEventListener("keyup", moveChatHistory);

    //Обработка отпраки сообщения по нажатию кнопки
    let sendMessageButton = document.getElementById("js-send-message-button");
    sendMessageButton.addEventListener("click", sendMessage);

    //Обработка отправки сообщения при нажатии на Enter
    document.addEventListener("keypress", function (ev) {
        if (ev.keyCode === 13) {
            sendMessage();
            ev.preventDefault();
        }
    });



    //Обработка выбора нового чата
    let usernames = allUsers.map(function (value) {
        return value.username;
    }).filter(function (username) {
        if (username === currentUser.username) {
            return false;
        }
        for (let i = 0; i < userChats.length; i++) {
            if (userChats[i].participants.length === 2 &&
                (userChats[i].participants[0].username === username ||
                    userChats[i].participants[1].username === username)) {
                return false;
            }
        }
        return true;
    });

    autocomplete(document.getElementById("js-search-chat-input"), usernames);

    //todo add csrf
    /*let metas = document.getElementsByTagName('meta');
    for (var i=0; i<metas.length; i++) {
        if (metas[i].getAttribute("name") === "_csrf") {
            token =  metas[i].getAttribute("content");
        }
        if (metas[i].getAttribute("name") === "_csrf_header") {
            header =  metas[i].getAttribute("content");
        }
    }*/
};


function openChat(chat) {
    hideMessages();
    activateChat(chat);

    let chatBody = document.getElementById("js-chat-body");
    chatBody.style.display = "block";
    currentChat = chat;

    document.getElementById("js-chat-body-header-name").innerText = getChatName(chat);
    if (chat.messages === undefined) {
        chat.messages = getChatMessages(chat);
    }
    showAllMessages(chat.messages);
    rewindHistoryToDown();
}


function closeChat() {
    let chatBody = document.getElementById("js-chat-body");
    hideMessages();
    chatBody.style.display = "none";

    let chats = document.getElementsByClassName("chat-item");
    for (var i = 0; i < chats.length; i++) {
        chats[i].classList.remove("active");
    }
}

function deleteMessage(message) {
    let xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/messages/" + message.messageId, false);
    // xhr.setRequestHeader(header, token);
    xhr.send();
    if (xhr.status === 200) {
    }
}

function activateChat(chat) {
    let chats = document.getElementsByClassName("chat-item");
    for (var i = 0; i < chats.length; i++) {
        if (getChatName(chat) !== chats[i].getElementsByClassName("chat-item__content_name")[0].innerText) {
            chats[i].classList.remove("active");
        } else {
            chats[i].classList.add('active');
        }
    }
}


function showChat(chat) {
    let chatList = document.getElementById("js-chat-list");
    let chatItemTmpl = document.getElementById("chatItemTemplate");
    let chatItem = chatItemTmpl.content.cloneNode(true);

    chatItem.getElementById("js-chat-item_name").innerText = getChatName(chat);
    chatItem.getElementById("js-chat-item-photo").setAttribute("src", getChatPhoto(chat));

    let chatLastMessage = getLastChatMessage(chat);

    if (chatLastMessage !== undefined) {
        chatItem.getElementById("js-chat-item_last-message").innerText =
            chatLastMessage.sender.userId === currentUser.userId ? "You: " + chatLastMessage.content : chatLastMessage.content;

        chatItem.getElementById("js-chat-item_time").innerText =
            dateFormatForLastMessage(new Date(chatLastMessage.time));
    }

    chatList.appendChild(chatItem);

    let userChat;
    userChats.forEach(function (value) {
        if (getChatName(value) === chatList.lastElementChild.querySelector(".chat-item__content_name").innerText) {
            userChat = value;
        }
    });

    chatList.lastElementChild.addEventListener("click", function () {
        openChat(userChat);
    });
}

function moveChatUp(chat) {
    let chatList = document.getElementById("js-chat-list");
    let chats = document.getElementsByClassName("chat-item");
    let chatItem;
    for (var i = 0; i < chats.length; i++) {
        if (getChatName(chat) === chats[i].getElementsByClassName("chat-item__content_name")[0].innerText) {
            chatItem = chats[i];
            chatList.removeChild(chatItem);
            chatList.insertBefore(chatItem, chatList.children[1]);
            break;
        }
    }

}

function dateFormatForLastMessage(messageTime) {
    let currentDate = new Date();
    let options;
    if (messageTime.getFullYear() !== currentDate.getFullYear()) {
        options = {year: 'numeric', month: 'long', day: 'numeric'}
    } else if (messageTime.getMonth() !== currentDate.getMonth() ||
        messageTime.getMonth() === currentDate.getMonth() && messageTime.getDay() !== currentDate.getDay()) {
        options = {month: 'long', day: 'numeric'}
    } else {
        options = {hour: 'numeric', minute: 'numeric'}
    }

    return messageTime.toLocaleString("en-US", options);
}

function sendMessage() {
    let messageInputText = document.getElementById("js-message-input-text");
    let text = messageInputText.innerText;
    text = text.trim();

    if (text === "") return;

    messageInputText.innerText = "";

    let message = {};
    message.time = new Date();
    message.sender = currentUser;
    message.content = text;
    message.chat = currentChat;
    message.isRead = false;


    stompClient.send("/app/message", {},
        JSON.stringify(message, function (key, value) {
            if (key === "chat") {
                return JSON.parse(JSON.stringify(currentChat, ["timeOfCreation", "chatId", "participants", "isRead"]));
            }
            return value;
        }));

}


function showLastMessage(chat, chatLastMessage) {
    let chats = document.getElementsByClassName("chat-item");
    let chatItem;
    for (var i = 0; i < chats.length; i++) {
        if (getChatName(chat) === chats[i].getElementsByClassName("chat-item__content_name")[0].innerText) {
            chatItem = chats[i];
        }
    }
    chatItem.querySelector("#js-chat-item_last-message").innerText =
        chatLastMessage.sender.userId === currentUser.userId ? "You: " + chatLastMessage.content : chatLastMessage.content;

    chatItem.querySelector("#js-chat-item_time").innerText = dateFormatForLastMessage(new Date(chatLastMessage.time));

}


function showMessage(message) {
    let chatHistory = document.getElementById("js-chat-history");
    let messageTmpl = document.getElementById("messageTemplate");
    let messageItem = messageTmpl.content.cloneNode(true);

    messageItem.getElementById("js-message-sender").innerText = message.sender.username;
    messageItem.getElementById("js-message-photo").setAttribute("src", message.sender.photo);
    messageItem.getElementById("js-message-text").innerText = message.content;
    let messageTime = new Date(message.time);
    messageItem.getElementById("js-message-time").innerText = messageTime.getHours() + ":" + messageTime.getMinutes();
    chatHistory.appendChild(messageItem);

    let elem = chatHistory.lastElementChild;

    elem.addEventListener("click", function (ev2) {
        deleteMessage(message);
        hideMessage(elem);
    });

    rewindHistoryToDown()
}

function showAllMessages(messages) {
    messages.forEach(function (value) {
        showMessage(value);
    })
}

function hideMessages() {
    let chatHistory = document.getElementById("js-chat-history");
    let elements = chatHistory.children;

    let i = 0;
    while (elements[i] !== undefined) {
        if (elements[i].className === 'message') {
            chatHistory.removeChild(elements[i])
        } else {
            i++;
        }
    }
}

function hideMessage(message) {
    let chatHistory = document.getElementById("js-chat-history");
    chatHistory.removeChild(message);
}

function getChatName(chat) {
    if (chat.participants.length > 2) {
        return chat.name;
    } else {
        if (chat.participants[0].userId === currentUser.userId) {
            return chat.participants[1].username;
        } else {
            return chat.participants[0].username;
        }
    }
}

function getChatPhoto(chat) {
    if (chat.participants.length > 2) {
        return chat.photo;
    } else {
        if (chat.participants[0].userId === currentUser.userId) {
            return chat.participants[1].photo;
        } else {
            return chat.participants[0].photo;
        }
    }
}

function getLastChatMessage(chat) {
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/chats/" + chat.chatId + "/messages/last", false);
    // xhr.setRequestHeader(header, token);
    xhr.send();
    if (xhr.status === 200) {
        return JSON.parse(xhr.responseText);
    }
}

function getChatMessages(chat) {
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/chats/" + chat.chatId + "/messages", false);
    // xhr.setRequestHeader(header, token);
    xhr.send();
    if (xhr.status === 200) {
        return JSON.parse(xhr.responseText);
    }
}


function rewindHistoryToDown() {
    chatHistory.scrollTop = chatHistory.scrollHeight;
}


function moveChatHistory() {
    let initialHeightOfMessageInput = messageInput.clientHeight;
    let initialPadddingOfChatHistory = Number(getComputedStyle(chatHistory).paddingBottom.slice(0, -2));

    chatHistory.style.paddingBottom = (initialPadddingOfChatHistory + (messageInput.clientHeight - initialHeightOfMessageInput)) + "px";
    rewindHistoryToDown();
}


function createChat(username) {
    let chat = {};
    for (let i = 0; i < allUsers.length; i++) {
        if (allUsers[i].username === username) {
            chat.participants = [currentUser, allUsers[i]];
            chat.isRead = true;
            chat.timeOfCreation = Date.now();
            break;
        }
    }

    fetch("/chats", {
        method: "POST",
        body: JSON.stringify(chat),
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
            // 'X-CSRF-Token': token
        }
    }).then(function (response) {
        if (response.ok) {
            response.json().then(function (responseChat) {
                let respChat = responseChat;
                userChats.push(respChat);
                openChat(respChat);
                showChat(respChat);
                activateChat(respChat);
            });
        }
    })
}


function autocomplete(inp, arr) {
    var currentFocus;

    inp.addEventListener("input", function (e) {
        var a, b, i, val = this.value;

        if (val === "") {
            document.getElementById("js-chat-list").style.display = "block";
        } else {
            document.getElementById("js-chat-list").style.display = "none";
        }

        closeAllLists();
        if (!val) {
            return false;
        }
        currentFocus = -1;

        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "search-chat-items");

        this.parentNode.appendChild(a);

        for (i = 0; i < arr.length; i++) {

            if (arr[i].substr(0, val.length).toUpperCase() === val.toUpperCase()) {
                b = document.createElement("DIV");
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";

                b.addEventListener("click", function (e) {
                    createChat(this.getElementsByTagName("input")[0].value);
                    inp.value = "";
                    document.getElementById("js-chat-list").style.display = "block";
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });

    function closeAllLists(elmnt) {
        var x = document.getElementsByClassName("search-chat-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt !== x[i] && elmnt !== inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
}