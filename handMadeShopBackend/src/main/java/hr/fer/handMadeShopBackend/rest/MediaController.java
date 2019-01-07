package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.domain.Advertisement;
import hr.fer.handMadeShopBackend.domain.Story;
import hr.fer.handMadeShopBackend.service.AdService;
import hr.fer.handMadeShopBackend.service.StoryService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@RestController
@RequestMapping("/media")
@Lazy
public class MediaController {

    public static final String BASE_URL_GET_STORY_IMAGE = "http://104.248.255.232:8080/media/story/image/";
    public static final String BASE_URL_GET_ADVERTISEMENT_IMAGE = "http://104.248.255.232:8080/media/advertisement/image/";
    public static final String BASE_URL_GET_STORY_VIDEO = "http://104.248.255.232:8080/media/story/video/";

    @Autowired
    private StoryService storyService;

    @Autowired
    private AdService adService;

    @GetMapping(value = "/story/image/download/{storyId}", headers = "Accept=image/jpeg, image/jpg, image/png")
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

    @PostMapping("/story/image/upload/{storyId}")
    public Story saveStoryImage(@RequestParam("file") MultipartFile filedata, @PathVariable("storyId") long storyId) {
        validateStoryExists(storyId);
        
        String url = Constants.IMAGE_BASE_URL_STORIES + "image" + storyId + ".png";
        try {
            byte[] bytes = filedata.getBytes();

            BufferedImage final_img = ImageIO.read(new ByteArrayInputStream(bytes));
            OutputStream os = Files.newOutputStream(Paths.get(url), StandardOpenOption.CREATE_NEW);
            ImageIO.write(final_img, "png", os);

            Story story = storyService.fetch(storyId);
            if(story != null) {
                story.setImageUrl(BASE_URL_GET_STORY_IMAGE + storyId);
                return storyService.save(story);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("The image could not be saved - " + e.getMessage());
        }
        return null;
    }

    @GetMapping(value = "/advertisement/image/download/{advertisementId}", headers = "Accept=image/jpeg, image/jpg, image/png")
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

    @PostMapping("/advertisement/image/upload/{advertisementId}")
    public Advertisement saveAdvertisementImage(@RequestParam("file") MultipartFile filedata, @PathVariable("advertisementId") long advertisementId) {
        validateAdvertisementExists(advertisementId);
        String url = Constants.IMAGE_BASE_URL_ADVERTISEMENTS + "image" + advertisementId+ ".png";

        try {
            byte[] bytes = filedata.getBytes();

            BufferedImage final_img = ImageIO.read(new ByteArrayInputStream(bytes));
            OutputStream os = Files.newOutputStream(Paths.get(url), StandardOpenOption.CREATE_NEW);
            ImageIO.write(final_img, "png", os);

            Advertisement advertisement = adService.fetch(advertisementId);
            if(advertisement != null) {
                advertisement.setImageURL(BASE_URL_GET_ADVERTISEMENT_IMAGE + advertisementId);
                return adService.save(advertisement);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("The image could not be saved - " + e.getMessage());
        }
        return null;
    }

    @GetMapping(value = "/story/video/download/{storyId}", headers = "Accept=video/mpeg, video/quicktime, video/mp4")
    public ResponseEntity<InputStreamResource> getStoryVideo(@PathVariable Long storyId, HttpServletResponse response) throws IOException {
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

    @PostMapping("/story/video/upload/{storyId}")
    public Story create(@PathVariable Long storyId, @RequestParam("file") MultipartFile file) {
        validateStoryExists(storyId);
        String url = Constants.VIDEO_BASE_URL_STORIES + "storyVideo" + storyId + ".mp4";

        try {
            byte[] bytes = file.getBytes();
            OutputStream os = Files.newOutputStream(Paths.get(url), StandardOpenOption.CREATE_NEW);
            os.write(bytes);

            Story story = storyService.fetch(storyId);
            if(story != null) {
                story.setVideoUrl(BASE_URL_GET_STORY_VIDEO + storyId);
                return storyService.save(story);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void validateStoryExists(long storyId) {
        Story story = storyService.fetch(storyId);
        if(story == null) {
            throw new IllegalArgumentException("The story with the specified id does not exist.");
        }
    }

    private void validateAdvertisementExists(long advertisementId) {
        Advertisement advertisement = adService.fetch(advertisementId);
        if(advertisement == null) {
            throw new IllegalArgumentException("The advertisement with the specified id does not exist.");
        }
    }

}
