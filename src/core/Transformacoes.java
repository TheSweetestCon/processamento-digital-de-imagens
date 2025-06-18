package src.core;

import java.awt.image.BufferedImage;
import src.utils.MathUtils;

public class Transformacoes {

    public Transformacoes(){

    }

    public static BufferedImage applyTranslate(BufferedImage originalImage, int dx, int dy) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage translatedImage = new BufferedImage(width, height, originalImage.getType());

        int[][] translateMatrix = {
            {1, 0, dx},
            {0, 1, dy},
            {0, 0, 1}
        };

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int [] originalCoord = {x, y, 1};
                int [] translatedCoord = MathUtils.multiplyMatrix(translateMatrix, originalCoord);

                int newX = translatedCoord[0];
                int newY = translatedCoord[1];

                if((newX >= 0) && (newX < width) && (newY >= 0) && (newY < height)){
                    int rgb = originalImage.getRGB(x, y);
                    translatedImage.setRGB(newX, newY, rgb);
                }
            }
        }

        return translatedImage;
    }

    public static BufferedImage applyRotation(BufferedImage originalImage, double degrees){
        double radianos = Math.toRadians(degrees);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        //Setar o centro da imagem para a rotação
        int centerX = width / 2;
        int centerY = height / 2;
        BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());
        
        
        // A matriz de translação 3x3
        double[][] matrizRotacao = {
            {Math.cos(radianos), -Math.sin(radianos), 0},
            {Math.sin(radianos), Math.cos(radianos), 0},
            {0, 0, 1}
        };
    
        // Percorrer todos os pixels da imagem original
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double[] coordenadaTransformada = {x - centerX, y - centerY, 1};


            double[] coordenadaOriginal = MathUtils.multiplyMatrixVectorDouble(matrizRotacao, coordenadaTransformada);

            // Ajuste de volta ao espaço original da imagem
            int originalX = (int) (coordenadaOriginal[0] + centerX);
            int originalY = (int) (coordenadaOriginal[1] + centerY);

            // Verificar se as coordenadas originais estão dentro dos limites
                if (originalX >= 0 && originalX < width && originalY >= 0 && originalY < height) {
                    int rgb = originalImage.getRGB(originalX, originalY);
                    rotatedImage.setRGB(x, y, rgb);
                }
            }
        }
        return rotatedImage;
    }

    private static BufferedImage applyMirror(BufferedImage originalImage, double[][] matrix){
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int centerX = width/2;
        int centerY = height/2;
        BufferedImage mirroredImage = new BufferedImage(width, height, originalImage.getType());
        

        for (int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                // Transformar coordenadas para o espaço centrado
                double[] coordenadaTransformada = {x - centerX, y - centerY, 1};

                // Aplicar a matriz de espelhamento
                double[] coordenadaOriginal = MathUtils.multiplyMatrixVectorDouble(matrix, coordenadaTransformada);

                // Ajustar de volta ao espaço da imagem
                int originalX = (int) (coordenadaOriginal[0] + centerX);
                int originalY = (int) (coordenadaOriginal[1] + centerY);

                // Verificar se as coordenadas originais estão dentro dos limites da imagem
                if ((originalX >= 0) && (originalX < width) && (originalY >= 0) && (originalY < height)) {
                    int rgb = originalImage.getRGB(originalX, originalY);
                    mirroredImage.setRGB(x, y, rgb);
                }
            }
        }
        return mirroredImage;
    }

    public static BufferedImage applyHorizontalMirror(BufferedImage originalImage){
        double[][] mirrorMatrix = {
            {-1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
        return applyMirror(originalImage, mirrorMatrix);
    }

    public static BufferedImage applyVerticalMirror(BufferedImage originalImage){
        double[][] mirrorMatrix = {
            {1, 0, 0},
            {0, -1, 0},
            {0, 0, 1}
        };
        return applyMirror(originalImage, mirrorMatrix);
    }

    public static BufferedImage applyScale(BufferedImage originalImage, double scale) {
        int larguraOriginal = originalImage.getWidth();
        int alturaOriginal = originalImage.getHeight();
        int novaLargura = (int) (larguraOriginal * scale);
        int novaAltura = (int) (alturaOriginal * scale);
    
        if (novaLargura <= 0) {
            novaLargura = 1;
        }
        if (novaAltura <= 0) {
            novaAltura = 1;
        }
    
        BufferedImage novaImagem = new BufferedImage(novaLargura, novaAltura, originalImage.getType());
    
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                double originalX = x / scale;
                double originalY = y / scale;
    
                // Interpolação bilinear
                int rgb = bilinearInterpolation(originalImage, originalX, originalY);
                novaImagem.setRGB(x, y, rgb);
            }
        }
        return novaImagem;
    }
    
    private static int bilinearInterpolation(BufferedImage image, double x, double y) {
        int largura = image.getWidth();
        int altura = image.getHeight();
    
        int x1 = (int) Math.floor(x);
        int y1 = (int) Math.floor(y);
        int x2 = (int) Math.ceil(x);
        int y2 = (int) Math.ceil(y);
    
        if (x1 < 0 || x1 >= largura || y1 < 0 || y1 >= altura ||
            x2 < 0 || x2 >= largura || y2 < 0 || y2 >= altura) {
            return 0; // Retorna preto se as coordenadas estiverem fora dos limites
        }
    
        double xWeight = x - x1;
        double yWeight = y - y1;
    
        int rgb11 = image.getRGB(x1, y1);
        int rgb12 = image.getRGB(x1, y2);
        int rgb21 = image.getRGB(x2, y1);
        int rgb22 = image.getRGB(x2, y2);
    
        int a11 = (rgb11 >> 24) & 0xff;
        int r11 = (rgb11 >> 16) & 0xff;
        int g11 = (rgb11 >> 8) & 0xff;
        int b11 = rgb11 & 0xff;
    
        int a12 = (rgb12 >> 24) & 0xff;
        int r12 = (rgb12 >> 16) & 0xff;
        int g12 = (rgb12 >> 8) & 0xff;
        int b12 = rgb12 & 0xff;
    
        int a21 = (rgb21 >> 24) & 0xff;
        int r21 = (rgb21 >> 16) & 0xff;
        int g21 = (rgb21 >> 8) & 0xff;
        int b21 = rgb21 & 0xff;
    
        int a22 = (rgb22 >> 24) & 0xff;
        int r22 = (rgb22 >> 16) & 0xff;
        int g22 = (rgb22 >> 8) & 0xff;
        int b22 = rgb22 & 0xff;
    
        int a = (int) (a11 * (1 - xWeight) * (1 - yWeight) + a12 * (1 - xWeight) * yWeight +
                    a21 * xWeight * (1 - yWeight) + a22 * xWeight * yWeight);
        int r = (int) (r11 * (1 - xWeight) * (1 - yWeight) + r12 * (1 - xWeight) * yWeight +
                    r21 * xWeight * (1 - yWeight) + r22 * xWeight * yWeight);
        int g = (int) (g11 * (1 - xWeight) * (1 - yWeight) + g12 * (1 - xWeight) * yWeight +
                    g21 * xWeight * (1 - yWeight) + g22 * xWeight * yWeight);
        int b = (int) (b11 * (1 - xWeight) * (1 - yWeight) + b12 * (1 - xWeight) * yWeight +
                    b21 * xWeight * (1 - yWeight) + b22 * xWeight * yWeight);
    
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
