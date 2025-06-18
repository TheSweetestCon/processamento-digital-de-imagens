package src.core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

import java.util.ArrayList;

public class Desafio {

    private static int totalPills = 0;
    private static int brokenPills = 0;
    private static int capsules = 0;
    private static int roundPills = 0;

    /* Analisa as formas em uma imagem para detectar e classificar comprimidos */
    public static BufferedImage analyzeShapes(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        boolean[][] visited = new boolean[height][width];

        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resultImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setStroke(new BasicStroke(2));

        // Reseta os contadores para cada nova imagem processada
        totalPills = 0;
        brokenPills = 0;
        capsules = 0;
        roundPills = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Se o pixel não foi visitado e é branco (parte de um comprimido)
                if (!visited[y][x] && isWhite(image, x, y)) {
                    totalPills++; // Cada nova região branca é um "comprimido" potencial inicialmente
                    analyzeRegion(image, visited, x, y, g2d); // Analisa a região conectada
                }
            }
        }

        // Imprime o relatório final no console
        System.out.println("--- Relatório Final ---");
        System.out.println("Total de comprimidos na esteira: " + totalPills);
        System.out.println("Comprimidos quebrados (não aproveitados): " + brokenPills);
        System.out.println("Cápsulas: " + capsules);
        System.out.println("Comprimidos redondos: " + roundPills);
        System.out.println("-----------------------");

        String mensagem = String.format(
            "Relatório Final\n" +
            "Total de comprimidos na esteira: %d\n" +
            "Comprimidos quebrados (não aproveitados): %d\n" +
            "Cápsulas: %d\n" +
            "Comprimidos redondos: %d",
            totalPills, brokenPills, capsules, roundPills
        );

        JOptionPane.showMessageDialog(null, mensagem, "Resultado da Análise", JOptionPane.INFORMATION_MESSAGE);

        g2d.dispose();
        return resultImage;
    }

    /* Analisa uma região conectada de pixels brancos para determinar o tipo de forma */
    private static void analyzeRegion(BufferedImage image, boolean[][] visited, int startX, int startY, Graphics2D g2d) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startX, startY));

        int minX = startX, maxX = startX;
        int minY = startY, maxY = startY;
        int area = 0;
        ArrayList<Point> regionPixels = new ArrayList<>(); // Armazena todos os pixels da região para cálculo de perímetro

        // Direções para BFS (8-conectividade para melhor preenchimento da região)
        int[] dx = {0, 0, 1, -1, 1, 1, -1, -1};
        int[] dy = {1, -1, 0, 0, 1, -1, 1, -1};

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int x = p.x;
            int y = p.y;

            // Verifica os limites da imagem
            if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) continue;
            // Se já foi visitado ou não é um pixel branco, pula
            if (visited[y][x] || !isWhite(image, x, y)) continue;

            visited[y][x] = true;
            area++;
            regionPixels.add(p);

            // Atualiza os limites da bounding box
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);

            // Adiciona os vizinhos à fila
            for (int i = 0; i < dx.length; i++) {
                queue.add(new Point(x + dx[i], y + dy[i]));
            }
        }

        // Calcula a largura e altura da bounding box
        int width = maxX - minX + 1;
        int height = maxY - minY + 1;

        // Calcula o perímetro da região
        int perimeter = 0;
        for (Point p : regionPixels) {
            int x = p.x;
            int y = p.y;
            boolean isBorder = false;

            // Verifica os 4 vizinhos diretos (não diagonais)
            // Se um vizinho não for branco, ou estiver fora dos limites da imagem, é um pixel de borda
            if (x + 1 < image.getWidth() && !isWhite(image, x + 1, y)) isBorder = true;
            if (x - 1 >= 0 && !isWhite(image, x - 1, y)) isBorder = true;
            if (y + 1 < image.getHeight() && !isWhite(image, x, y + 1)) isBorder = true;
            if (y - 1 >= 0 && !isWhite(image, x, y - 1)) isBorder = true;

            // Considera também pixels na borda da imagem como borda da forma
            if (x == 0 || x == image.getWidth() - 1 || y == 0 || y == image.getHeight() - 1) {
                isBorder = true;
            }

            if (isBorder) {
                perimeter++;
            }
        }

        double aspectRatio = (double) width / height;
        double circularity = (perimeter == 0) ? 0 : (4 * Math.PI * area) / (Math.pow(perimeter, 2));
        double boundingBoxArea = width * height;

        // Fill Ratio: Quão preenchida a bounding box está pela área da forma.
        double fillRatio = (boundingBoxArea == 0) ? 0 : (double) area / boundingBoxArea; 

        String shapeType;
        Color color;
        
        // Alta circularidade e aspect ratio próxima de 1.
        if (circularity > 0.88 && aspectRatio > 0.9 && aspectRatio < 1.1) {
            shapeType = "Círculo";
            color = Color.GREEN;
            roundPills++;
        }
        // Circularidade baixa ou Aspect ratio muito extrema
        else if (circularity < 0.40 ||  fillRatio < 0.50 || (aspectRatio > 2.2 || aspectRatio < 0.45)) { 
            shapeType = "Quebrado";
            color = Color.RED;
            brokenPills++;
        }
        // Circularidade dentro de uma faixa esperada para cápsulas e aspect ratio de uma cápsula normal
        else if (circularity > 0.6 && circularity <= 1.10 && (aspectRatio >= 0.6 && aspectRatio <= 1.8)) { 
            shapeType = "Cápsula";
            color = Color.BLUE;
            capsules++;
        }
        else {
             shapeType = "Quebrado (Residual)";
             color = Color.MAGENTA;
             brokenPills++;
        }


        int minAreaThreshold = 50; 
        if (area < minAreaThreshold) {
            shapeType = "Ruído/Pequeno";
            color = Color.GRAY;
            // Se for ruído, decrementa o total de "comprimidos" contados inicialmente.
            totalPills--; 
            // E ajusta os contadores específicos caso ele tenha sido classificado antes.
            if (shapeType.equals("Círculo")) roundPills--;
            else if (shapeType.equals("Cápsula")) capsules--;
            else if (shapeType.equals("Quebrado")) brokenPills--;
            else if (shapeType.equals("Quebrado (Residual)")) brokenPills--;
        }


        // Desenha o retângulo delimitador e o rótulo na imagem de resultado
        g2d.setColor(color);
        g2d.drawRect(minX, minY, width, height);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(shapeType + " (Area: " + area + ")", minX, Math.max(minY - 5, 12));

        // Imprime os detalhes da região detectada no console
        System.out.println("Região detectada:");
        System.out.println("   Área: " + area);
        System.out.println("   Perímetro: " + perimeter);
        System.out.println("   Largura: " + width + ", Altura: " + height);
        System.out.printf("   Razão de aspecto: %.2f\n", aspectRatio);
        System.out.printf("   Circularidade: %.2f\n", circularity);
        System.out.printf("   Fill Ratio: %.2f\n", fillRatio);
        System.out.println("   Forma identificada: " + shapeType);
        System.out.println();

    }

    private static boolean isWhite(BufferedImage img, int x, int y) {
        Color c = new Color(img.getRGB(x, y));
        int threshold = 220;
        return c.getRed() >= threshold && c.getGreen() >= threshold && c.getBlue() >= threshold;
    }
}