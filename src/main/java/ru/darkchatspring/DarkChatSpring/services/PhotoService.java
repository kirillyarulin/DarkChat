package ru.darkchatspring.DarkChatSpring.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

@Service
public class PhotoService {

    public String getRandomAvatar(boolean isMale) {
        final Random random = new Random();
        String path = "src/main/resources/static/img/avatars/default-avatars/" + (isMale ? "male" : "female");

        File[] photos = new File(path).listFiles();

        if (photos != null) {
            String randomPhoto = photos[random.nextInt(photos.length)].getAbsolutePath();
            return randomPhoto.substring(randomPhoto.indexOf("img"));
        }
        return null;
    }


}
