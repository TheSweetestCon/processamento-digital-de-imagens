package src.core;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Morfologia {
    public Morfologia() {
        
    }

    public static BufferedImage applyDilation(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
    
        BufferedImage dilatedImage = new BufferedImage(width, height, originalImage.getType());
    
        int[][] matrix = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
    
        int matrixHeight = matrix.length / 2;
        int matrixWidth = matrix[0].length / 2;
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int maxA = 0;
                int maxR = 0;
                int maxG = 0;
                int maxB = 0;
    
                for (int i = -matrixHeight; i <= matrixHeight; i++) {
                    for (int j = -matrixWidth; j <= matrixWidth; j++) {
                        int newY = y + i;
                        int newX = x + j;
    
                        if ((newX >= 0) && (newX < width) && (newY >= 0) && (newY < height) && (matrix[i + matrixHeight][j + matrixWidth] == 1)) {
                            int rgba = originalImage.getRGB(newX, newY);
                            Color corVizinha = new Color(rgba, true);
                            maxA = Math.max(maxA, corVizinha.getAlpha());
                            maxR = Math.max(maxR, corVizinha.getRed());
                            maxG = Math.max(maxG, corVizinha.getGreen());
                            maxB = Math.max(maxB, corVizinha.getBlue());
                        }
                    }
                }
    
                Color newColor = new Color(maxR, maxG, maxB, maxA);
                dilatedImage.setRGB(x, y, newColor.getRGB());
            }
        }
    
        return dilatedImage;
    }

    public static BufferedImage applyErosion(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
    
        BufferedImage erodedImage = new BufferedImage(width, height, originalImage.getType());
    
        int[][] matrix = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
    
        int matrixHeight = matrix.length / 2;
        int matrixWidth = matrix[0].length / 2;
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int minA = 255;
                int minR = 255;
                int minG = 255;
                int minB = 255;
    
                for (int i = -matrixHeight; i <= matrixHeight; i++) {
                    for (int j = -matrixWidth; j <= matrixWidth; j++) {
                        int newY = y + i;
                        int newX = x + j;
    
                        if ((newX >= 0) && (newX < width) && (newY >= 0) && (newY < height) && (matrix[i + matrixHeight][j + matrixWidth] == 1)) {
                            int rgba = originalImage.getRGB(newX, newY);
                            Color corVizinha = new Color(rgba, true);
                            minA = Math.min(minA, corVizinha.getAlpha());
                            minR = Math.min(minR, corVizinha.getRed());
                            minG = Math.min(minG, corVizinha.getGreen());
                            minB = Math.min(minB, corVizinha.getBlue());
                        }
                    }
                }
    
                Color newColor = new Color(minR, minG, minB, minA);
                erodedImage.setRGB(x, y, newColor.getRGB());
            }
        }
    
        return erodedImage;
    }

    public static BufferedImage applyOpening(BufferedImage originalImage) {
        BufferedImage eroded = applyErosion(originalImage);
        BufferedImage openedImaged = applyDilation(eroded);
        return openedImaged;
    }
    
    public static BufferedImage applyClosing(BufferedImage originalImage) {
        BufferedImage dilated = applyDilation(originalImage);
        BufferedImage closedImage = applyErosion(dilated);
        return closedImage;
    }
    
    
    
}
