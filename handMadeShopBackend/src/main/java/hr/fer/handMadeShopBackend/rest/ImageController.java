package hr.fer.handMadeShopBackend.rest;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/images")
@Lazy
public class ImageController {

    @GetMapping(value = "/story/{storyId}", headers = "Accept=image/jpeg, image/jpg, image/png")
    public byte[] getImage(@PathVariable("storyId") int storyId) {
        if(!Files.exists(Paths.get("src/main/resources/images/storyImage" + storyId + ".png"))) {
            throw new IllegalArgumentException("The file does not exist");
        }
        ClassPathResource backImgFile = new ClassPathResource("images/storyImage" + storyId + ".png");
        try {
            InputStream is = backImgFile.getInputStream();
            BufferedImage bfimage = ImageIO.read(is);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bfimage, "png", baos);
            baos.flush();
            byte[] img_in_bytes = baos.toByteArray();
            baos.close();
            return img_in_bytes;
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @PostMapping("/story/{storyId}")
    public void saveImage(@RequestParam("file") MultipartFile filedata, @PathVariable String storyId) {
        try {
            byte[] bytes = filedata.getBytes();

            BufferedImage final_img = ImageIO.read(new ByteArrayInputStream(bytes));
            OutputStream os = Files.newOutputStream(Paths.get("src/main/resources/images/storyImage" + storyId + ".png"), StandardOpenOption.CREATE_NEW);
            ImageIO.write(final_img, "png", os);

        } catch (IOException e) {
            throw new IllegalArgumentException("The image could not be saved - " + e.getMessage());
        }
    }
}
