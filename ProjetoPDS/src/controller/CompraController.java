package controller;

import model.Produto;
import model.ProdutoDAO;
import view.TelaCompras;
import javax.swing.JOptionPane;
import java.util.List;

public class CompraController {
    private final TelaCompras view;
    private final ProdutoDAO model;
    private final Navegador navegador;
    private double totalGeral = 0.0;

    public CompraController(TelaCompras view, ProdutoDAO model, Navegador navegador) {
        this.view      = view;
        this.model     = model;
        this.navegador = navegador;

        atualizarVitrine();

        this.view.acaoAdicionarCarrinho(e -> executarAdicionar());
        this.view.acaoMenuAdicionar(e -> executarAdicionar());

        this.view.acaoRemoverCarrinho(e -> executarRemover());
        this.view.acaoMenuRemover(e -> executarRemover());

        this.view.acaoFinalizarCompra(e -> {
            if (view.getModCarrinho().getRowCount() == 0) {
                view.exibirAlerta("O carrinho está vazio!");
                return;
            }

            try {
                StringBuilder nota = new StringBuilder();
                nota.append("----- NOTA FISCAL -----\n");
                for (int i = 0; i < view.getModCarrinho().getRowCount(); i++) {
                    String item   = view.getModCarrinho().getValueAt(i, 1).toString();
                    int    qtd    = (int) view.getModCarrinho().getValueAt(i, 2);
                    String subStr = view.getModCarrinho().getValueAt(i, 3).toString();
                    double sub    = Double.parseDouble(subStr.replace("R$", "").replace(",", ".").trim());
                    nota.append(String.format("%s (x%d) - R$ %.2f\n", item, qtd, sub));
                }
                nota.append("---------------------------\n");
                nota.append(String.format("TOTAL: R$ %.2f", totalGeral));

                JOptionPane.showMessageDialog(view, nota.toString(), "Nota Fiscal Emitida",
                    JOptionPane.INFORMATION_MESSAGE);

                limparSessao();
                navegador.navegarPara("LOGIN");

            } catch (Exception ex) {
                view.exibirAlerta("Erro ao emitir nota fiscal. Tente novamente.");
            }
        });

        this.view.acaoLogout(e -> {
            limparSessao();
            this.navegador.navegarPara("LOGIN");
        });
    }

    private void executarAdicionar() {
        int linha = view.getTabelaProdutos().getSelectedRow();
        if (linha == -1) {
            view.exibirAlerta("Selecione um produto na vitrine!");
            return;
        }

        try {
            int    id           = (int) view.getModVitrine().getValueAt(linha, 0);
            String nome         = view.getModVitrine().getValueAt(linha, 1).toString();
            String precoStr     = view.getModVitrine().getValueAt(linha, 2).toString();
            double preco        = Double.parseDouble(precoStr.replace("R$", "").replace(",", ".").trim());
            int    estoqueAtual = (int) view.getModVitrine().getValueAt(linha, 3);

            if (estoqueAtual <= 0) {
                view.exibirAlerta("Produto sem estoque disponível!");
                return;
            }

            String input = JOptionPane.showInputDialog(view,
                "Produto: " + nome +
                "\nEstoque disponível: " + estoqueAtual +
                "\nQuantos deseja adicionar ao carrinho?",
                "Adicionar ao Carrinho", JOptionPane.QUESTION_MESSAGE);

            if (input == null) return; 
            input = input.trim();
            if (input.isEmpty()) {
                view.exibirAlerta("Informe uma quantidade!");
                return;
            }

            int qtdDesejada;
            try {
                qtdDesejada = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                view.exibirAlerta("Quantidade inválida! Digite apenas números inteiros.");
                return;
            }

            if (qtdDesejada <= 0) {
                view.exibirAlerta("A quantidade deve ser maior que zero!");
                return;
            }
            if (qtdDesejada > estoqueAtual) {
                view.exibirAlerta("Quantidade indisponível! Estoque atual: " + estoqueAtual);
                return;
            }

            adicionarOuIncrementar(id, nome, preco, qtdDesejada);
            model.editar(new Produto(id, nome, preco, estoqueAtual - qtdDesejada));
            atualizarVitrine();
            atualizarTotal();

        } catch (NumberFormatException ex) {
            view.exibirAlerta("Erro inesperado ao ler o preço do produto. Tente novamente.");
        }
    }

    private void executarRemover() {
        int linha = view.getTabelaCarrinho().getSelectedRow();
        if (linha == -1) {
            view.exibirAlerta("Selecione um item no carrinho para remover!");
            return;
        }

        try {
            int id            = (int) view.getModCarrinho().getValueAt(linha, 0);
            int qtdNoCarrinho = (int) view.getModCarrinho().getValueAt(linha, 2);
            String nomeProd   = view.getModCarrinho().getValueAt(linha, 1).toString();

            String input = JOptionPane.showInputDialog(view,
                "Produto: " + nomeProd +
                "\nQuantidade no carrinho: " + qtdNoCarrinho +
                "\nQuantos deseja remover?",
                "Remover do Carrinho", JOptionPane.QUESTION_MESSAGE);


            input = input.trim();
            if (input.isEmpty()) {
                view.exibirAlerta("Informe uma quantidade!");
                return;
            }

            int qtdRemover;
            try {
                qtdRemover = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                view.exibirAlerta("Quantidade inválida! Digite apenas números inteiros.");
                return;
            }

            if (qtdRemover <= 0) {
                view.exibirAlerta("A quantidade deve ser maior que zero!");
                return;
            }
            if (qtdRemover > qtdNoCarrinho) {
                view.exibirAlerta("Você só tem " + qtdNoCarrinho + " unidade(s) deste item no carrinho!");
                return;
            }

            Produto pRef = model.listarTodos().stream()
                .filter(p -> p.getId() == id).findFirst().orElse(null);

            if (pRef != null) {
                model.editar(new Produto(id, pRef.getNome(), pRef.getPreco(),
                    pRef.getQuantidade() + qtdRemover));
            }

            int qtdRestante = qtdNoCarrinho - qtdRemover;
            if (qtdRestante == 0) {
                view.getModCarrinho().removeRow(linha);
            } else {
                String subStr        = view.getModCarrinho().getValueAt(linha, 3).toString();
                double subAtual      = Double.parseDouble(subStr.replace("R$", "").replace(",", ".").trim());
                double precoUnitario = subAtual / qtdNoCarrinho;
                double novoSubtotal  = precoUnitario * qtdRestante;
                view.getModCarrinho().setValueAt(qtdRestante, linha, 2);
                view.getModCarrinho().setValueAt(String.format("R$ %.2f", novoSubtotal), linha, 3);
            }

            atualizarVitrine();
            atualizarTotal();

        } catch (Exception ex) {
            view.exibirAlerta("Erro ao remover item do carrinho. Tente novamente.");
        }
    }

    private void adicionarOuIncrementar(int id, String nome, double preco, int qtd) {
        for (int i = 0; i < view.getModCarrinho().getRowCount(); i++) {
            if ((int) view.getModCarrinho().getValueAt(i, 0) == id) {
                int    qtdAtual     = (int) view.getModCarrinho().getValueAt(i, 2);
                int    novaQtd      = qtdAtual + qtd;
                double novoSubtotal = novaQtd * preco;
                view.getModCarrinho().setValueAt(novaQtd, i, 2);
                view.getModCarrinho().setValueAt(String.format("R$ %.2f", novoSubtotal), i, 3);
                return;
            }
        }
        view.getModCarrinho().addRow(new Object[]{id, nome, qtd, String.format("R$ %.2f", preco * qtd)});
    }

    private void atualizarTotal() {
        totalGeral = 0.0;
        try {
            for (int i = 0; i < view.getModCarrinho().getRowCount(); i++) {
                String subStr = view.getModCarrinho().getValueAt(i, 3).toString();
                double sub    = Double.parseDouble(subStr.replace("R$", "").replace(",", ".").trim());
                totalGeral += sub;
            }
        } catch (NumberFormatException ex) {
            view.exibirAlerta("Erro ao calcular o total do carrinho.");
            totalGeral = 0.0;
        }
        view.getLblTotalValor().setText(String.format("Total: R$ %.2f", totalGeral));
    }

    private void limparSessao() {
        view.getModCarrinho().setRowCount(0);
        totalGeral = 0.0;
        view.getLblTotalValor().setText("Total: R$ 0.00");
    }

    public void atualizarVitrine() {
        view.getModVitrine().setRowCount(0);
        List<Produto> lista = model.listarTodos();
        for (Produto p : lista) {
            String precoFormatado = String.format("R$ %.2f", p.getPreco());
            view.getModVitrine().addRow(new Object[]{p.getId(), p.getNome(), precoFormatado, p.getQuantidade()});
        }
    }
}