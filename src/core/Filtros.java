package src.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Filtros {

    public static void aplicarFiltro() {
        System.out.println("Filtro aplicado");
    }

    public static BufferedImage applyGrayscale(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                Color color = new Color(rgb, true);
                int alpha = color.getAlpha();
                
                if (alpha == 0) {
                    grayImage.setRGB(x, y, rgb);
                    continue;
                }
                
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                
                int gray = (int) Math.round(0.299 * red + 0.587 * green + 0.114 * blue);
                gray = Math.min(Math.max(gray, 0), 255);
                
                Color grayColor = new Color(gray, gray, gray, alpha);
                grayImage.setRGB(x, y, grayColor.getRGB());
            }
        }
        return grayImage;
    }

    public static BufferedImage applyBrightConstrast(BufferedImage originalImage, double brightness, double contrast) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage adjustedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                Color color = new Color(rgb, true);
                int alpha = color.getAlpha();
    
                if (alpha == 0) { // Mantém a transparência
                    adjustedImage.setRGB(x, y, rgb);
                    continue;
                }
    
                // Ajuste de brilho e contraste nos canais de cor
                int red = (int) ((color.getRed() - 128) * contrast + 128 + brightness);
                int green = (int) ((color.getGreen() - 128) * contrast + 128 + brightness);
                int blue = (int) ((color.getBlue() - 128) * contrast + 128 + brightness);
    
                // Garante que os valores fiquem entre 0 e 255
                red = Math.min(Math.max(red, 0), 255);
                green = Math.min(Math.max(green, 0), 255);
                blue = Math.min(Math.max(blue, 0), 255);
    
                Color newColor = new Color(red, green, blue, alpha);
                adjustedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return adjustedImage;
    }

    public static BufferedImage applyLowPass(BufferedImage originalImage) {
    int width = originalImage.getWidth();
    int height = originalImage.getHeight();
    BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
    int kernelSize = 5; // Tamanho da janela de vizinhança (3x3)
    int edge = kernelSize / 2; // Para evitar acessar posições inválidas

    for (int y = edge; y < height - edge; y++) {
        for (int x = edge; x < width - edge; x++) {
            List<Integer> reds = new ArrayList<>();
            List<Integer> greens = new ArrayList<>();
            List<Integer> blues = new ArrayList<>();
            int alpha = new Color(originalImage.getRGB(x, y), true).getAlpha(); // Mantém a transparência

            // Coletar os valores dos pixels vizinhos
            for (int ky = -edge; ky <= edge; ky++) {
                for (int kx = -edge; kx <= edge; kx++) {
                    int rgb = originalImage.getRGB(x + kx, y + ky);
                    Color color = new Color(rgb, true);
                    reds.add(color.getRed());
                    greens.add(color.getGreen());
                    blues.add(color.getBlue());
                }
            }

            // Ordenar e pegar a mediana
            Collections.sort(reds);
            Collections.sort(greens);
            Collections.sort(blues);
            int medianIndex = reds.size() / 2;
            Color medianColor = new Color(reds.get(medianIndex), greens.get(medianIndex), blues.get(medianIndex), alpha);

            // Definir o novo pixel
            filteredImage.setRGB(x, y, medianColor.getRGB());
        }
    }

    return filteredImage;
    }

    public static BufferedImage applyHighPass(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
        int[][] kernel = {
            {-1, -1, -1},
            {-1,  12, -1},
            {-1, -1, -1}
        };
    
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sum = 0;
                int alpha = new Color(originalImage.getRGB(x, y), true).getAlpha();
    
                if (alpha == 0) {
                    filteredImage.setRGB(x, y, originalImage.getRGB(x, y));
                    continue;
                }
    
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int rgb = originalImage.getRGB(x + kx, y + ky);
                        Color c = new Color(rgb, true);
                        int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                        sum += gray * kernel[ky + 1][kx + 1];
                    }
                }
    
                int value = Math.min(Math.max(sum + 128, 0), 255);
                Color newColor = new Color(value, value, value, alpha);
                filteredImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return filteredImage;
    }
    
    public static BufferedImage applyThreshold(BufferedImage originalImage, int threshold) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage thresholdImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                Color color = new Color(rgb, true);
                int alpha = color.getAlpha();
    
                if (alpha == 0) {
                    thresholdImage.setRGB(x, y, rgb);
                    continue;
                }
    
                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                int binary = (gray >= threshold) ? 255 : 0;
                Color bw = new Color(binary, binary, binary, alpha);
                thresholdImage.setRGB(x, y, bw.getRGB());
            }
        }
    
        return thresholdImage;
    }
    


}


