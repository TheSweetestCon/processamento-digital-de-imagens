package src.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import src.core.Transformacoes;
import src.core.Desafio;
import src.core.Filtros;
import src.core.Morfologia;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDIFrame extends JFrame {
    private ImagePanel images;
    private JPanel histogramComparePanel;
    private HistogramPanel originalHistogramPanel;
    private HistogramPanel modifiedHistogramPanel;


    public PDIFrame() {
        setTitle("Processamento de Imagem - [Emanuel Vogel. Matheus Haag]");
        setSize(1600, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new MenuBar(this);
        setJMenuBar(menuBar);

        images = new ImagePanel();

        originalHistogramPanel = new HistogramPanel(images.getOriginalImage(), "Original");
        modifiedHistogramPanel = new HistogramPanel(images.getmodifiedImage(), "Modificada");

        histogramComparePanel = new JPanel(new GridLayout(1, 2));
        histogramComparePanel.add(originalHistogramPanel);
        histogramComparePanel.add(modifiedHistogramPanel);

        add(histogramComparePanel, BorderLayout.SOUTH);


        add(images.getPanel(), BorderLayout.CENTER);
    }

    public BufferedImage getImage(){
        if(!images.validateModifiedImage()){
            return images.getOriginalImage();
        }
        return images.getmodifiedImage();
    }

    private void updateHistograms() {
        originalHistogramPanel.setImage(images.getOriginalImage());
        modifiedHistogramPanel.setImage(images.getmodifiedImage());
    }
    

    public void translateImage() {
        if(!images.validateOriginalImage()){
            JOptionPane.showMessageDialog(this, "Nenhuma imagem foi carregada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BufferedImage translatedImage = this.getImage();

        // Caixa de diálogo para o usuário inserir o valor de dx e dy
        String inputX = JOptionPane.showInputDialog(this, "Informe o valor do deslocamento no eixo X (horizontal):", "Translação X", JOptionPane.PLAIN_MESSAGE);
        String inputY = JOptionPane.showInputDialog(this, "Informe o valor do deslocamento no eixo Y (vertical):", "Translação Y", JOptionPane.PLAIN_MESSAGE);
    
        try {
            // Converter os valores de entrada para inteiros
            int dx = Integer.parseInt(inputX);
            int dy = Integer.parseInt(inputY);

            translatedImage = Transformacoes.applyTranslate(translatedImage, dx, dy);
            images.setModifiedImage(translatedImage);
            updateHistograms();
    
            // Chamar a função para transladar a imagem com os valores informados
            //transladarImagem(dx, dy);
        } catch (NumberFormatException e) {
            // Exibir mensagem de erro caso o valor inserido não seja um número inteiro válido
            JOptionPane.showMessageDialog(this, "Os valores informados não são válidos. Por favor, insira números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rotateImage(){
        BufferedImage rotatedImage = this.getImage();
        String inputDegree = JOptionPane.showInputDialog(this, "Informe Quantos graus você quer rotacionar a imagem:", "Rotação", JOptionPane.PLAIN_MESSAGE);
    
        try {
            // Converter os valores de entrada para inteiros
            double graus = Double.parseDouble(inputDegree);
    

            rotatedImage = Transformacoes.applyRotation(rotatedImage, graus);
            images.setModifiedImage(rotatedImage);
            updateHistograms();

        } catch (NumberFormatException e) {
            // Exibir mensagem de erro caso o valor inserido não seja um número inteiro válido
            JOptionPane.showMessageDialog(this, "Os valores informados não são válidos. Por favor, insira números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mirrorImage(){
        String[] options = {"Horizontal", "Vertical"};
        int choice = JOptionPane.showOptionDialog(this, "Escolha o tipo de espelhamento:", "Espelhar imagem", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        BufferedImage mirroredImage = this.getImage();

        try {
            if(choice == -1){
                System.out.println("Entrou -1");
                return;
            } else if (choice == 0) {
                System.out.println("Entrou 0");
                mirroredImage = Transformacoes.applyHorizontalMirror(mirroredImage);
            } else {
                System.out.println("Entrou 1");
                mirroredImage = Transformacoes.applyVerticalMirror(mirroredImage);
            }
            images.setModifiedImage(mirroredImage);
            updateHistograms();
        } catch (Exception e) {
            // TODO: handle exception
            JOptionPane.showMessageDialog(this, "Os valores informados não são válidos. Por favor, insira números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        
        
    
    }

    public void scaleUpImage(){
        String inputEscala = JOptionPane.showInputDialog(this, "Informe o fator de escala (>1 para aumentar):", "Aumentar Imagem", JOptionPane.PLAIN_MESSAGE);

        try {
            BufferedImage scaledImage = this.getImage();
            double scale = Double.parseDouble(inputEscala);
            if (scale <= 1) {
                JOptionPane.showMessageDialog(this, "O fator de escala deve ser maior que 1.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            scaledImage = Transformacoes.applyScale(scaledImage, scale);
            images.setModifiedImage(scaledImage);
            updateHistograms();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void scaleDownImage() {
        String inputEscala = JOptionPane.showInputDialog(this, "Informe o fator de escala (0 < escala < 1):", "Diminuir Imagem", JOptionPane.PLAIN_MESSAGE);
    
        try {
            BufferedImage scaledImage = this.getImage();
            double scale = Double.parseDouble(inputEscala);
    
            // Verifica se o fator de escala está dentro do intervalo permitido
            if (scale <= 0 || scale >= 1) {
                JOptionPane.showMessageDialog(this, "O fator de escala deve estar entre 0 e 1.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            scaledImage = Transformacoes.applyScale(scaledImage, scale);
            images.setModifiedImage(scaledImage);
            updateHistograms();
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toGrayscale(){
        
        try {
            BufferedImage image = this.getImage();
    
            image = Filtros.applyGrayscale(image);
            images.setModifiedImage(image);
            updateHistograms();
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void brightAndContrast(){
        String bright = JOptionPane.showInputDialog(this, "Informe o valor do ajuste no brilho da imagem:", "Brilho X", JOptionPane.PLAIN_MESSAGE);
        String contrast = JOptionPane.showInputDialog(this, "Informe o valor do ajuste no contraste da imagem:", "Constraste", JOptionPane.PLAIN_MESSAGE);

        Double brightInt = Double.parseDouble(bright);
        Double contrastInt = Double.parseDouble(contrast);
        try{
            BufferedImage image = this.getImage();
    
            image = Filtros.applyBrightConstrast(image,brightInt,contrastInt);
            images.setModifiedImage(image);
            updateHistograms();
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void lowPassFilter(){
        try{
            BufferedImage image = this.getImage();
    
            image = Filtros.applyLowPass(image);
            images.setModifiedImage(image);
            updateHistograms();
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void highPassFilter(){
        try{
        BufferedImage image = this.getImage();
    
            image = Filtros.applyHighPass(image);
            images.setModifiedImage(image);
            updateHistograms();
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void thresholdFilter(){
        String threshold = JOptionPane.showInputDialog(this, "Informe o valor do threshold:", "Threshold", JOptionPane.PLAIN_MESSAGE);

        int vThreshold = Integer.parseInt(threshold);
        try{
        BufferedImage image = this.getImage();
    
            image = Filtros.applyThreshold(image, vThreshold);
            images.setModifiedImage(image);
            updateHistograms();
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dilation() {
        try{
            BufferedImage image = this.getImage();
        
            image = Morfologia.applyDilation(image);
            images.setModifiedImage(image);
            updateHistograms();
        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void erosion(){
        try{
            BufferedImage image = this.getImage();
        
            image = Morfologia.applyErosion(image);
            images.setModifiedImage(image);
            updateHistograms();
        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void opening(){
        try{
            BufferedImage image = this.getImage();
        
            image = Morfologia.applyOpening(image);
            images.setModifiedImage(image);
            updateHistograms();
        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void closing(){
        try{
            BufferedImage image = this.getImage();
        
            image = Morfologia.applyClosing(image);
            images.setModifiedImage(image);
            updateHistograms();
        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void desafio(){
        try{
            BufferedImage image = this.getImage();
            image = Filtros.applyGrayscale(image);
            image = Filtros.applyLowPass(image);
            image = Filtros.applyThreshold(image,75);
            image = Desafio.analyzeShapes(image);
            images.setModifiedImage(image);
            updateHistograms();
        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void about() {
        JOptionPane.showMessageDialog(this, "Processamento de Imagem\nAutores: [Emanuel Vogel e Matheus Haag]\nVersão: 1.0", "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }

    public void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage originalImage = ImageIO.read(file);
                images.setOriginalImage(originalImage);

                BufferedImage modifiedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
                images.setModifiedImage(modifiedImage);

                Graphics g = modifiedImage.getGraphics();
                g.drawImage(originalImage, 0, 0, null);
                g.dispose();

                updateHistograms();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir a imagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PNG (*.png)", "png");

        // Defina o filtro como o filtro selecionado padrão
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();

            if(!filePath.toLowerCase().endsWith(".png")){
                file = new File(filePath + ".png");
            }

            try {
                ImageIO.write(images.getmodifiedImage(), "png", file);
                JOptionPane.showMessageDialog(this, "Imagem salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar a imagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}