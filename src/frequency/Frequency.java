/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frequency;

import frequency.utils.SUtils;
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author luke
 */
public class Frequency extends JFrame{

    
    private final GroupLayout fullLayout;
    private final GroupLayout groupHeader;
    private final JPanel panelHeader;
    private final JLabel lbHearder;
    private final JLabel lbClose;
    private final GroupLayout groupContent;
    private final JPanel panelContent;
    private final GroupLayout groupUpload;
    private final JPanel panelUpload;
    private final JLabel lbUploadText;
    private final JLabel lbUploadImg;
    private final GroupLayout groupFrequency;
    private final JPanel panelFrequency;
    private final JLabel lbResult;
    private final JButton saveButton;
    private final JLabel lbMsg;
    private String fileSound;
    private BufferedImage image;
    private int markerPosition = 0;
    private Color markerColor = Color.white;
    private int xy;
    private int xx;
    
    public Frequency(){
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);
        
        
        image = new BufferedImage( 1, 1, BufferedImage.TYPE_4BYTE_ABGR );
        BufferedImage img = new BufferedImage( 383, 209, BufferedImage.TYPE_4BYTE_ABGR );
        Graphics2D g = (Graphics2D)img.getGraphics();
        g.setColor( Color.black );
        g.fillRect( 0, 0, 383, 209 );
        g.dispose();
        image = img;	
        panelFrequency = new JPanel() {
            
            @Override
            public void paintComponent( Graphics g )
            {			
                    super.paintComponent(g);
                    synchronized( image )
                    {
                            g.drawImage( image, 0, 0, null );
                            g.setColor( markerColor );
                            g.drawLine( markerPosition, 0, markerPosition, image.getHeight() );
                    }

                    Thread.yield();


                    this.repaint();
            }

            @Override
            public void update(Graphics g){
                    paint(g);
            }

            @Override
            public Dimension getPreferredSize()
            {
                    return new Dimension( image.getWidth(), image.getHeight( ) );
            }
        };
        
        
        panelHeader = new JPanel();
        panelHeader.setBackground(new Color(255, 255, 255));
        panelHeader.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                panelHeaderMouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                panelHeaderMousePressed(e);
            }
        });
        lbClose = new JLabel();
        lbClose.setFont(new Font("Segoe UI", 0, 24));
        lbClose.setText("X");
        lbClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                lbCloseMouseClicked(e);
            }
        });
        lbHearder = new JLabel();
        lbHearder.setFont(new Font("Segoe UI", 0, 24));
        lbHearder.setText("Phân tích tần số");//Phân tích tần số //Frequency Analysis
        groupHeader = new GroupLayout(panelHeader);
        panelHeader.setLayout(groupHeader);
        groupHeader.setHorizontalGroup(
            groupHeader.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupHeader.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lbHearder)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 473, Short.MAX_VALUE)
                .addComponent(lbClose)
                .addGap(26, 26, 26))
        );
        groupHeader.setVerticalGroup(
            groupHeader.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupHeader.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lbClose)
                .addContainerGap(41, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, groupHeader.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbHearder)
                .addContainerGap())
        );
        
        panelUpload = new JPanel();
        panelUpload.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                panelUploadMouseDragged(e);
            }
}       );
        panelUpload.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                panelUploadMouseClicked(e);
            }
        });
        lbUploadText = new JLabel();
        lbUploadText.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lbUploadText.setForeground(new java.awt.Color(11, 181, 217));
        lbUploadText.setText("Chọn tập tin tải lên");//Chọn tập tin tải lên //Drop Files to upload
        lbUploadImg = new JLabel();
        lbUploadImg.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lbUploadImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frequency/upload_48px_1.png")));
        groupUpload = new GroupLayout(panelUpload);
        panelUpload.setLayout(groupUpload);
        groupUpload.setHorizontalGroup(
            groupUpload.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupUpload.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(lbUploadText)
                .addContainerGap(82, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, groupUpload.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbUploadImg)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupUpload.setVerticalGroup(
            groupUpload.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, groupUpload.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(lbUploadImg)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbUploadText)
                .addGap(105, 105, 105))
        );
        
        groupFrequency = new GroupLayout(panelFrequency);
        panelFrequency.setLayout(groupFrequency);
        groupFrequency.setHorizontalGroup(
            groupFrequency.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );
        groupFrequency.setVerticalGroup(
            groupFrequency.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );
        lbResult = new JLabel();
        lbResult.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lbResult.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveButton = new JButton();
        saveButton.setBackground(new java.awt.Color(21, 113, 254));
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Lưu tập tin");//Save Files
        saveButton.setEnabled(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save(e);
            }
        });
        lbMsg = new JLabel();
        lbMsg.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lbMsg.setForeground(new java.awt.Color(255,0,0));
        
        panelContent = new JPanel();
        panelContent.setBackground(new Color(255, 255, 255));
        groupContent = new GroupLayout(panelContent);
        panelContent.setLayout(groupContent);
        groupContent.setHorizontalGroup(
            groupContent.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupContent.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(groupContent.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbMsg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(groupContent.createSequentialGroup()
                        .addComponent(panelUpload, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(groupContent.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelFrequency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGroup(groupContent.createSequentialGroup()
                                .addComponent(lbResult, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveButton)))))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupContent.setVerticalGroup(
            groupContent.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupContent.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(groupContent.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(groupContent.createSequentialGroup()
                        .addComponent(panelFrequency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(groupContent.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(lbResult, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saveButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(panelUpload, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        
        
        fullLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(fullLayout);
        fullLayout.setHorizontalGroup(
            fullLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panelHeader, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelContent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        fullLayout.setVerticalGroup(
            fullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fullLayout.createSequentialGroup()
                    .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }
    
    private void panelHeaderMouseDragged(MouseEvent e){
        int x = e.getXOnScreen();
        int y = e.getYOnScreen();
        this.setLocation(x-xx,y-xy);
    }
    
    private void panelHeaderMousePressed(MouseEvent e){
        xx = e.getX();
        xy = e.getY();
    }
    
    private void lbCloseMouseClicked(MouseEvent e){
        System.exit(0);
    }
    
    private void panelUploadMouseDragged(MouseEvent e){
        panelUpload.setBackground(Color.lightGray);
    }
    
    private void panelUploadMouseClicked(MouseEvent e){
        SUtils.clear(image);
        lbResult.setText("");
        lbMsg.setText("");
        saveButton.setEnabled(false);
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio & Video Files", "mp3", "wav", "3gp", "mp4", "mov");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(panelUpload);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + fc.getSelectedFile().getName());
            System.out.println("You chose to open this file length: " + fc.getSelectedFile().length());
            String inputPath = fc.getSelectedFile().getPath();
            
            
            File mp3File;
            mp3File = new File(inputPath);
            //Check file nhập vào có phải là file video hay không. 
            if(SUtils.isFilenameVideoSupported(mp3File.getName())){
                //Nếu là file video thì sẽ tách âm thanh từ video ra một file mp3.
                mp3File = SUtils.getAudioFromVideo(inputPath);
            }
            String fileName = mp3File.getName();
            //Check file âm thanh đưa vào nếu hỗ trợ phân tích thì chuyển tiếp đến bước tiếp theo.
            if(SUtils.isFilenameSupported(fileName)){
                if(mp3File != null){
                    //Cut một giây từ file  âm thanh.
                    fileSound = SUtils.cutAudio(mp3File);
                    if(!fileSound.equals("")){
                        float[] samples;
                        //Kiểm tra định dạng file âm thanh đã cut 
                        if(fileSound.toLowerCase().contains("mp3".toLowerCase())){
                            //Phân tích âm thanh từ file âm thanh mp3 trả về một mảng tần số
                            samples = SUtils.readAllFrequency(fileSound);
                        }else{
                            //Phân tích âm thanh từ file âm thanh wav trả về một mảng tần số
                            samples = SUtils.readWavFrequency(fileSound);
                        }
                        //Ghi tần số phân tích được vào giao diện.
                        SUtils.writeFrequency(samples, Color.red, image, 1024, panelFrequency);
                        //Check mảng tần số tăng hay giảm.
                        if(SUtils.checkSamples(samples)){
                            System.out.println("Tan so tang dan");//Ascending frequency
                            lbResult.setText("Tần số tăng dần.");//Tần số tăng dần.
                        }else{
                            System.out.println("Tan so giam dan");//Descending frequency.
                            lbResult.setText("Tần số giảm dần.");//Tần số giảm dần.
                        }
                        saveButton.setEnabled(true);
                    }else{
                        lbMsg.setText("Tập tin được chon quá lớn.");
                    }
                }else{
                    lbMsg.setText("Tập tin bạn chọn đã có lỗi.");
                }
            }else{
                lbMsg.setText("Tập tin bạn chọn không được hỗ trợ.");
            }
            
        }
    }
    
    
    // Action lưu tập tin kết quả.
    private void save(ActionEvent e){
        JFileChooser fs = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio & Video Files", "mp3", "wav", "3gp", "mp4", "mov");
        fs.setFileFilter(filter);
        fs.setSelectedFile(new File(fileSound));
        if (e.getSource() == saveButton) {
            if(!fileSound.equals("")){
                int returnVal = fs.showSaveDialog(Frequency.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    String fullPath = fs.getCurrentDirectory().toString() + "/" + fs.getSelectedFile().getName();
                    File fileDest = new File(fullPath);
                    File fileCut = new File(fileSound);
                    SUtils.saveFileMp3(fileCut, fileDest);
                    System.out.println("Saving: " + fullPath);

                } else {
                    System.out.println("Save command cancelled by user.");
                }
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
                if("Nimbus".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }   
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.out.println("LookAndFeels : " + e.getMessage());
        }
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Frequency().setVisible(true);
            }
        });
        
    }
    
}
