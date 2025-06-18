package src.ui;
import javax.swing.*;
@SuppressWarnings("unused")

public class MenuBar extends JMenuBar {

   
    public MenuBar(PDIFrame pdiFrame) {
        JMenu fileMenu = new JMenu("Arquivo");
        JMenu transformMenu = new JMenu("Transformações Geométricas");
        JMenu filtersMenu = new JMenu("Filtros");
        JMenu morfMenu = new JMenu("Morfologias");
        JMenu extractMenu = new JMenu("Extração de Características");
        
        JMenuItem openImage = fileMenu.add(new JMenuItem("Abrir Imagem"));
        openImage.addActionListener(e -> pdiFrame.openImage());
        JMenuItem saveImage = fileMenu.add(new JMenuItem("Salvar Imagem"));
        saveImage.addActionListener(e -> pdiFrame.saveImage());
        JMenuItem  about = fileMenu.add(new JMenuItem("Sobre"));
        about.addActionListener(e -> pdiFrame.about());
        JMenuItem exit = fileMenu.add(new JMenuItem("Sair"));
        exit.addActionListener(e -> System.exit(0));

        JMenuItem translate = transformMenu.add(new JMenuItem("Transladar"));
        translate.addActionListener(e -> pdiFrame.translateImage());
        JMenuItem rotate = transformMenu.add(new JMenuItem("Rotacionar"));
        rotate.addActionListener(e -> pdiFrame.rotateImage());
        JMenuItem mirror = transformMenu.add(new JMenuItem("Espelhar"));
        mirror.addActionListener(e -> pdiFrame.mirrorImage());
        JMenuItem scaleUp = transformMenu.add(new JMenuItem("Aumentar"));
        scaleUp.addActionListener(e -> pdiFrame.scaleUpImage());
        JMenuItem scaleDown = transformMenu.add(new JMenuItem("Diminuir"));
        scaleDown.addActionListener(e -> pdiFrame.scaleDownImage());

        JMenuItem grayscale = filtersMenu.add(new JMenuItem("Grayscale"));
        grayscale.addActionListener(e -> pdiFrame.toGrayscale());
        JMenuItem brightContrast = filtersMenu.add(new JMenuItem("Brilho e Contraste"));
        brightContrast.addActionListener(e -> pdiFrame.brightAndContrast());
        JMenuItem passaBaixa = filtersMenu.add(new JMenuItem("Passa Baixa"));
        passaBaixa.addActionListener(e -> pdiFrame.lowPassFilter());
        JMenuItem passaAlta = filtersMenu.add(new JMenuItem("Passa Alta"));
        passaAlta.addActionListener(e -> pdiFrame.highPassFilter());
        JMenuItem threshold = filtersMenu.add(new JMenuItem("Threshold"));
        threshold.addActionListener(e -> pdiFrame.thresholdFilter());

        JMenuItem dilatacao = morfMenu.add(new JMenuItem("Dilatação"));
        dilatacao.addActionListener(e -> pdiFrame.dilation());
        JMenuItem erosao = morfMenu.add(new JMenuItem("Erosão"));
        erosao.addActionListener(e -> pdiFrame.erosion());
        JMenuItem abertura = morfMenu.add(new JMenuItem("Abertura"));
        abertura.addActionListener(e -> pdiFrame.opening());
        JMenuItem fechamento = morfMenu.add(new JMenuItem("Fechamento"));
        fechamento.addActionListener(e -> pdiFrame.closing());

        JMenuItem desafio = extractMenu.add(new JMenuItem("Desafio"));
        desafio.addActionListener(e -> pdiFrame.desafio());
        
        add(fileMenu);
        add(transformMenu);
        add(filtersMenu);
        add(morfMenu);
        add(extractMenu);

    }

    
    
}
