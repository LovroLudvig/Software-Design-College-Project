package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.Constants.Constants;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/media")
@Lazy
public class MediaController {

    @GetMapping(value = "/story/image/{storyId}", headers = "Accept=image/jpeg, image/jpg, image/png")
    public byte[] getStoryImage(@PathVariable("storyId") long storyId) {
        String url = Constants.IMAGE_BASE_URL_STORIES + "image" + storyId + ".png";

        if(!Files.exists(Paths.get(url))) {
            throw new IllegalArgumentException("The file does not exist");
        }
        try {
            InputStream is = Files.newInputStream(Paths.get(url));
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

    @PostMapping("/story/image/{storyId}")
    public void saveStoryImage(@RequestParam("file") MultipartFile filedata, @PathVariable("storyId") long storyId) {
        String url = Constants.IMAGE_BASE_URL_STORIES + "image" + storyId + ".png";
        try {
            byte[] bytes = filedata.getBytes();

            BufferedImage final_img = ImageIO.read(new ByteArrayInputStream(bytes));
            OutputStream os = Files.newOutputStream(Paths.get(url), StandardOpenOption.CREATE_NEW);
            ImageIO.write(final_img, "png", os);

        } catch (IOException e) {
            throw new IllegalArgumentException("The image could not be saved - " + e.getMessage());
        }
    }

    @GetMapping(value = "/advertisement/image/{advertisementId}", headers = "Accept=image/jpeg, image/jpg, image/png")
    public byte[] getAdvertisementImage(@PathVariable("advertisementId") long advertisementId) {
        String url = Constants.IMAGE_BASE_URL_ADVERTISEMENTS + "image" + advertisementId+ ".png";

        if(!Files.exists(Paths.get(url))) {
            throw new IllegalArgumentException("The file does not exist");
        }
        try {
            InputStream is = Files.newInputStream(Paths.get(url));
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

    @PostMapping("/advertisement/image/{advertisementId}")
    public void saveAdvertisementImage(@RequestParam("file") MultipartFile filedata, @PathVariable("advertisementId") long advertisementId) {
        String url = Constants.IMAGE_BASE_URL_ADVERTISEMENTS + "image" + advertisementId+ ".png";
        try {
            byte[] bytes = filedata.getBytes();

            BufferedImage final_img = ImageIO.read(new ByteArrayInputStream(bytes));
            OutputStream os = Files.newOutputStream(Paths.get(url), StandardOpenOption.CREATE_NEW);
            ImageIO.write(final_img, "png", os);

        } catch (IOException e) {
            throw new IllegalArgumentException("The image could not be saved - " + e.getMessage());
        }
    }

    @GetMapping(value = "/story/video/{storyId}", headers = "Accept=video/mpeg, video/quicktime, video/mp4")
    public ResponseEntity<InputStreamResource> getStoryVideo(@PathVariable int storyId, HttpServletResponse response) throws IOException {
        String url = Constants.VIDEO_BASE_URL_STORIES + "storyVideo" + storyId + ".mp4";

        MediaType mediaType = MediaType.parseMediaType("video/mp4");
        Path p = Paths.get(url);
        InputStream is = Files.newInputStream(p);
        InputStreamResource resource = new InputStreamResource(is);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + p.getFileName())
                .contentType(mediaType)
                .contentLength(p.toFile().length())
                .body(resource);
    }

    @PostMapping("/story/video/{storyId}")
    public void create(@PathVariable int storyId, @RequestParam("file") MultipartFile file) {
        String url = Constants.VIDEO_BASE_URL_STORIES + "storyVideo" + storyId + ".mp4";
        try {
            byte[] bytes = file.getBytes();
            OutputStream os = Files.newOutputStream(Paths.get(url), StandardOpenOption.CREATE_NEW);
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
