// let header;
// let token;

window.onload = function (ev) {

    // let metas = document.getElementsByTagName('meta');
    // for (var i=0; i<metas.length; i++) {
    //     if (metas[i].getAttribute("name") === "_csrf") {
    //         token =  metas[i].getAttribute("content");
    //     }
    //     if (metas[i].getAttribute("name") === "_csrf_header") {
    //         header =  metas[i].getAttribute("content");
    //     }
    // }

    document.getElementById("js-form").onsubmit = function (ev2) {
        return false;
    }

    let signupButton = document.getElementById("js-signup-btn");
    signupButton.addEventListener("click", registerUser);

    document.getElementById("js-alert-container").addEventListener("click", function (evt) {
        document.getElementById("js-alert-container").style.display = "none";
        document.getElementById("js-dark-background").style.display = "none";
        document.getElementById("js-alert-invalid-username").style.display = "none";
        document.getElementById("js-invalid-confirm-password").style.display = "none";
        document.getElementById("js-password").value = "";
        document.getElementById("js-confirm-password").value = "";
    })


};


function registerUser() {
    let usernameField = document.getElementById("js-username");
    let passwordField = document.getElementById("js-password");
    let confirmPasswordField = document.getElementById("js-confirm-password")

    if (/^[A-Za-z][A-Za-z0-9_\-.]{2,31}$/.test(usernameField.value) &&
        /^.{6,64}$/.test(passwordField.value) &&
        /^.{6,64}$/.test(confirmPasswordField.value)) {

        if (passwordField.value !== confirmPasswordField.value) {
            document.getElementById("js-alert-container").style.display = "flex";
            document.getElementById("js-dark-background").style.display = "block";
            document.getElementById("js-invalid-confirm-password").style.display = "inline-block";
            return;
        }

        let user = {};
        user.username = usernameField.value;
        user.password = passwordField.value;
        user.confirmPassword = confirmPasswordField.value;

        user.isMale = document.getElementById("js-gender-male").checked;

        photo = document.getElementById("js-url-photo").value;
        if (photo !== "") {
            user.photo = photo;
        }
        //todo
        // user.photoFile = document.getElementById("input-photo-file").files[0];


        fetch("/signup", {
            method: "POST",
            body: JSON.stringify(user),
            headers: {
                'Content-Type': 'application/json'
                // "X-CSRF-TOKEN": token
            }
        }).then(function (response) {
            console.log(response);
            if (response.ok) {
                window.location.replace("/login?signup=" + user.username);
            } else {
                usernameField.value = "";
                document.getElementById("js-alert-container").style.display = "flex";
                document.getElementById("js-dark-background").style.display = "block";
                document.getElementById("js-alert-invalid-username").style.display = "inline-block";
            }
        });
    }

}