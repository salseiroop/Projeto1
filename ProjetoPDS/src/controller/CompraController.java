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
        this.view = view;
        this.model = model;
        this.navegador = navegador;

        atualizarVitrine();

        this.view.acaoAdicionarCarrinho(e -> {
            int linha = view.getTabelaProdutos().getSelectedRow();
            if (linha != -1) {
                int id = (int) view.getModVitrine().getValueAt(linha, 0);
                String nome = view.getModVitrine().getValueAt(linha, 1).toString();
                
                // AJUSTE: Pegamos o preço formatado e limpamos para converter em double
                String precoStr = view.getModVitrine().getValueAt(linha, 2).toString();
                double preco = Double.parseDouble(precoStr.replace("R$", "").replace(",", ".").trim());
                
                int estoqueAtual = (int) view.getModVitrine().getValueAt(linha, 3);

                if (estoqueAtual > 0) {
                    adicionarOuIncrementar(id, nome, preco);
                    model.editar(new Produto(id, nome, preco, estoqueAtual - 1));
                    atualizarVitrine();
                    atualizarTotal();
                } else {
                    view.exibirAlerta("Produto sem estoque disponível!");
                }
            } else {
                view.exibirAlerta("Selecione um produto na vitrine!");
            }
        });

        this.view.acaoRemoverCarrinho(e -> {
            int linha = view.getTabelaCarrinho().getSelectedRow();
            if (linha != -1) {
                int id = (int) view.getModCarrinho().getValueAt(linha, 0);
                int qtdNoCarrinho = (int) view.getModCarrinho().getValueAt(linha, 2);
                
                Produto pRef = model.listarTodos().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
                
                if (pRef != null) {
                    model.editar(new Produto(id, pRef.getNome(), pRef.getPreco(), pRef.getQuantidade() + qtdNoCarrinho));
                }

                view.getModCarrinho().removeRow(linha);
                atualizarVitrine();
                atualizarTotal();
            } else {
                view.exibirAlerta("Selecione um item no carrinho para remover!");
            }
        });

        this.view.acaoFinalizarCompra(e -> {
            if (view.getModCarrinho().getRowCount() == 0) {
                view.exibirAlerta("O carrinho está vazio!");
                return;
            }

            StringBuilder nota = new StringBuilder();
            nota.append("----- NOTA FISCAL -----\n");
            for (int i = 0; i < view.getModCarrinho().getRowCount(); i++) {
                String item = view.getModCarrinho().getValueAt(i, 1).toString();
                int qtd = (int) view.getModCarrinho().getValueAt(i, 2);
                
                // AJUSTE: Limpeza do subtotal formatado para a Nota Fiscal
                String subStr = view.getModCarrinho().getValueAt(i, 3).toString();
                double sub = Double.parseDouble(subStr.replace("R$", "").replace(",", ".").trim());
                
                nota.append(String.format("%s (x%d) - R$ %.2f\n", item, qtd, sub));
            }
            nota.append("---------------------------\n");
            nota.append(String.format("TOTAL: R$ %.2f", totalGeral));

            JOptionPane.showMessageDialog(view, nota.toString(), "Nota Fiscal Emitida", JOptionPane.INFORMATION_MESSAGE);
            
            limparSessao();
            navegador.navegarPara("LOGIN");
        });

        this.view.acaoLogout(e -> {
            limparSessao();
            this.navegador.navegarPara("LOGIN");
        });
    }

    private void adicionarOuIncrementar(int id, String nome, double preco) {
        for (int i = 0; i < view.getModCarrinho().getRowCount(); i++) {
            if ((int)view.getModCarrinho().getValueAt(i, 0) == id) {
                int qtdAtual = (int) view.getModCarrinho().getValueAt(i, 2);
                int novaQtd = qtdAtual + 1;
                double novoSubtotal = novaQtd * preco;
                
                view.getModCarrinho().setValueAt(novaQtd, i, 2);
                // AJUSTE: Salva o subtotal formatado no carrinho
                view.getModCarrinho().setValueAt(String.format("R$ %.2f", novoSubtotal), i, 3);
                return;
            }
        }
        // AJUSTE: Adiciona nova linha com o subtotal formatado
        view.getModCarrinho().addRow(new Object[]{id, nome, 1, String.format("R$ %.2f", preco)});
    }

    private void atualizarTotal() {
        totalGeral = 0.0;
        for (int i = 0; i < view.getModCarrinho().getRowCount(); i++) {
            // AJUSTE: Para somar o total, limpamos a formatação de cada linha do carrinho
            String subStr = view.getModCarrinho().getValueAt(i, 3).toString();
            double sub = Double.parseDouble(subStr.replace("R$", "").replace(",", ".").trim());
            totalGeral += sub;
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
            // FORMATAÇÃO: Preço da vitrine agora com R$
            String precoFormatado = String.format("R$ %.2f", p.getPreco());
            view.getModVitrine().addRow(new Object[]{p.getId(), p.getNome(), precoFormatado, p.getQuantidade()});
        }
    }
}