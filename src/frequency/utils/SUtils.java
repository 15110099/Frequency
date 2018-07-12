/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frequency.utils;

import frequency.io.MP3Decoder;
import frequency.io.WaveDecoder;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 *
 * @author luke
 */
public class SUtils {
    
    public static boolean checkArray(List<Float> found){
        for(int i=0;i<found.size()-1;i++)
            if(found.get(i)>found.get(i + 1))
                return false;
        return true;
    }
    
    public static boolean checkSamples(float[] samples){
        for(int i=0; i<samples.length-1; i++){
            if(samples[i] > samples[i + 1]){
                return false;
            }
        }
        return true;
    }
    
    public static String[] getSupportedExtensions() {
        return new String[] {"mp3", "wav"};
    }
    
    public static String[] getSupportedVideoExtensions() {
        return new String[] {"3gp", "mp4", "mov"};
    }

    public static boolean isFilenameSupported(String filename) {
        filename = filename.toLowerCase();
        String[] extensions = getSupportedExtensions();
        for (String extension : extensions) {
            if (filename.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFilenameVideoSupported(String filename) {
        filename = filename.toLowerCase();
        String[] extensions = getSupportedVideoExtensions();
        for (String extension : extensions) {
            if (filename.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }
    
    public static File getAudioFromVideo(String filePath){
        try {
            File source = new File(filePath);
            String sourceName = source.getName();
            String name = sourceName.substring(0, sourceName.lastIndexOf('.'));
            String targetName = "samples/" + name + ".mp3";
            File target = new File(targetName);
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);
            Encoder encoder = new Encoder();
            encoder.encode(source, target, attrs);
            
            return target;
        } catch (IllegalArgumentException | EncoderException ex) {
            Logger.getLogger(SUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static int getDurationWithMp3Spi(File file){
        try{
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            if (fileFormat instanceof TAudioFileFormat) {
                Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
                String key = "duration";
                Long microseconds = (Long) properties.get(key);
                int mili = (int) (microseconds / 1000);
                return mili;
                
            } else {
                throw new UnsupportedAudioFileException();
            }
        }catch (UnsupportedAudioFileException | IOException e){
            System.out.println("getDurationWithMp3Spi - " + e.getMessage());
            return 0;
        }

    }
    
    public static double getDurationWav(File file){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames+0.0) / format.getFrameRate();
            return durationInSeconds;
        }catch (UnsupportedAudioFileException | IOException e){
            System.out.println("getDurationWav" + e.getLocalizedMessage());
            return 0.0;
        }
    }
    
    @SuppressWarnings("UnnecessaryBoxing")
    public static String cutAudio(File file){
        
        try {
            // Đọc file thành một đối tượng AudioSamples 
            AudioSamples as = new AudioSamples(file,"",false);
            String targetName = file.getName();
            
            double end = 0.0;
            double start = 0.0;
            //Lấy thời lượng của file âm thanh.
            if(!targetName.toLowerCase().contains("mp3".toLowerCase())){
                double durationTime  = getDurationWav(file);
                end = durationTime;
                start = durationTime - 1;
            }else{
                int endtime = getDurationWithMp3Spi(file);
                end = endtime / 1000;
                start = (endtime / 1000) - 1;
            }
            
            // Lấy ra thời lượng một giây cuối của file âm thanh.
            double[][] samples = as.getSamplesChannelSegregated(start, end);

            //Set lại một giây âm thanh cho AudioSamples.
            as.setSamples(samples);
            
            // Ghi đối tượng thành một file wav.
            File newFile = new File("samples/newAudioFileName.wav");
            as.saveAudio(newFile, true, AudioFileFormat.Type.WAVE, false);
            
            //Check file nhập vào lúc đầu. nếu là file mp3 thì convert wav file thu được sang file mp3.
            if(targetName.toLowerCase().contains("mp3".toLowerCase())){
                System.out.println("target name: " + targetName);
                String newTargetName = "samples/"+targetName;
                File target = new File(newTargetName);
                AudioAttributes audio = new AudioAttributes();
                audio.setCodec("libmp3lame");
                audio.setBitRate(new Integer(128000));
                audio.setChannels(new Integer(2));
                audio.setSamplingRate(new Integer(44100));
                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setFormat("mp3");
                attrs.setAudioAttributes(audio);
                Encoder encoder = new Encoder();
                encoder.encode(newFile, target, attrs);

                int rtime = getDurationWithMp3Spi(target);
                int rsec = (rtime / 1000) % 60;
                int rmin = (rtime / 1000) / 60;
                System.out.println("Time of output : " + rmin + ":" + rsec);
                System.out.println("File name output: " + target.getName());
                return newTargetName;
            }else{
                return "samples/newAudioFileName.wav";
            }
        } catch (Exception e) {
            //System.out.println("message :" + e.getMessage());
            return "";
        }   
    }
    
    /*
        Lưu tập tin kết quả.
    */
    @SuppressWarnings({"UnusedAssignment", "CallToPrintStackTrace"})
    public static void saveFileMp3(File source, File dest){
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("err saveFileMp3 : " + e.getMessage());
        }
    }
    
    public static float[] readAllFrequency( String file )
    {
        float[] samples = new float[1024];
        try{
            //Lấy ra các properties của file âm thanh sau khi cut.
            MP3Decoder decoder = new MP3Decoder( new FileInputStream( file ) );
            
            //Khởi tạo một list rỗng chứa các tần số.
            @SuppressWarnings("Convert2Diamond")
            ArrayList<Float> allSamples = new ArrayList<Float>( );
            
            //Đọc tần số từ file âm thanh và thêm vào list đã tạo.
            while( decoder.readSamples( samples ) > 0 )
            {
                    for( int i = 0; i < samples.length; i++ )
                            allSamples.add( samples[i] );
            }

            samples = new float[allSamples.size()];
            for( int i = 0; i < samples.length; i++ ){
                samples[i] = allSamples.get(i);
            }
            
            return samples;
        }catch (Exception e){
            return samples;
        }
    }
    
    public static float[] readWavFrequency(String file){
        float[] samples = new float[1024];
        try {
            //Lấy ra các properties của file âm thanh sau khi cut.
            WaveDecoder decoder = new WaveDecoder( new FileInputStream(file) );
            
            //Khởi tạo một list rỗng chứa các tần số.
            @SuppressWarnings("Convert2Diamond")
            ArrayList<Float> allSamples = new ArrayList<Float>( );
            
            //Đọc tần số từ file âm thanh và thêm vào list đã tạo.
            while( decoder.readSamples( samples ) > 0 )
            {
                    for( int i = 0; i < samples.length; i++ ){
                        allSamples.add( samples[i] );
                    }
            }

            samples = new float[allSamples.size()];
            for( int i = 0; i < samples.length; i++ ){
                samples[i] = allSamples.get(i);
            }
            return samples;
        }catch (Exception e){
            System.out.println("readWavFrequency : " + e.getMessage());
            return samples;
        }
    }
    
    private static float scalingFactor = 1;
    private static boolean cleared = true;
    
    public static void writeFrequency(float[] samples, final Color color, BufferedImage image, final float samplesPerPixel, JPanel panel){
        synchronized( image )
        {						
            if( image.getWidth() <  samples.length / samplesPerPixel )
            {
                    image = new BufferedImage( (int)(samples.length / samplesPerPixel), panel.getHeight(), BufferedImage.TYPE_4BYTE_ABGR );
                    Graphics2D g = image.createGraphics();
                    g.setColor( Color.black );
                    g.fillRect( 0, 0, image.getWidth(), image.getHeight() ); 
                    g.dispose();
                    panel.setSize( image.getWidth(), image.getHeight( ));
            }

            if(cleared)
            {
                float min = 0;
                float max = 0;
                for( int i = 0; i < samples.length; i++ )
                {
                        min = Math.min( samples[i], min );
                        max = Math.max( samples[i], max );
                }
                scalingFactor = max - min;
                cleared = false;
            }

            Graphics2D g = image.createGraphics();
            g.setColor( color );
            float lastValue = (samples[0] / scalingFactor) * image.getHeight() / 3 + image.getHeight() / 2;
            for( int i = 1; i < samples.length; i++ )
            {
                    float value = (samples[i] / scalingFactor) * image.getHeight() / 3 + image.getHeight() / 2;
                    g.drawLine( (int)((i-1) / samplesPerPixel), image.getHeight() - (int)lastValue, (int)(i / samplesPerPixel), image.getHeight() - (int)value );
                    lastValue = value;
            }
            g.dispose();											
        }	
    }
    
    public static void clear(BufferedImage image){
        Graphics2D g = image.createGraphics();
        g.setColor( Color.black );
        g.fillRect( 0, 0, image.getWidth(), image.getHeight() );
        g.dispose();
        cleared = true;
    }
}
