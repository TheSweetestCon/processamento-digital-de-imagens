package src.ui;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class ImagePanel {
    private JLabel labelOriginalImage, labelModifiedImage;
    private BufferedImage originalImage, modifiedImage;
    public JPanel createImages;
    

    public ImagePanel() {
        createImages = new JPanel(new GridLayout(1, 2));

        labelOriginalImage = new JLabel();
        labelOriginalImage.setHorizontalAlignment(SwingConstants.CENTER);
        createImages.add(labelOriginalImage);

        labelModifiedImage = new JLabel();
        labelModifiedImage.setHorizontalAlignment(SwingConstants.CENTER);
        createImages.add(labelModifiedImage);
    }
    
    public void setOriginalImage(BufferedImage originalImage){
        this.originalImage = originalImage;
        labelOriginalImage.setIcon(new ImageIcon(originalImage));
        
    }

    public void setModifiedImage(BufferedImage modifiedImage){
        this.modifiedImage = modifiedImage;
        labelModifiedImage.setIcon(new ImageIcon(modifiedImage));
    }
    

    public BufferedImage getOriginalImage(){
        return originalImage;
    }

    public BufferedImage getmodifiedImage(){
        return modifiedImage;
    }

    public boolean validateOriginalImage(){
        if(originalImage == null){
            return false;
        }
        return true;
    }

    public boolean validateModifiedImage(){
        if(modifiedImage == null){
            return false;
        }
        return true;
    }
    
    public JPanel getPanel(){
        return createImages;
    }


}