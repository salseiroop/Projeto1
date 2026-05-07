package controller;

import model.Produto;
import model.ProdutoDAO;
import view.TelaEstoque;
import java.util.List;

public class EstoqueController {
    private final TelaEstoque view;
    private final ProdutoDAO model;
    private final Navegador navegador;

    public EstoqueController(TelaEstoque view, ProdutoDAO model, Navegador navegador) {
        this.view = view;
        this.model = model;
        this.navegador = navegador;

        this.view.getTabela().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = view.getTabela().getSelectedRow();
                if (linha != -1) {
                    String nome = view.getModeloTabela().getValueAt(linha, 1).toString();
                    String preco = view.getModeloTabela().getValueAt(linha, 2).toString();
                    String qtd = view.getModeloTabela().getValueAt(linha, 3).toString();

                    view.setNomeProduto(nome);
                    view.setPreco(preco);
                    view.setQuantidade(qtd);
                }
            }
        });

        this.view.acaoAdicionarProduto(e -> {
            try {
                String nome = view.getNomeProduto();
                int qtd = Integer.parseInt(view.getQuantidade());
                double preco = Double.parseDouble(view.getPreco().replace(",", "."));

                model.salvar(new Produto(nome, preco, qtd));
                
                view.exibirAlerta("Produto salvo com sucesso!");
                view.limparCampos();
                atualizarTabela();
            } catch (Exception ex) {
                view.exibirAlerta("Erro nos dados: Verifique valores e quantidades.");
            }
        });

        this.view.acaoExcluir(e -> {
            int linha = view.getTabela().getSelectedRow();
            if (linha != -1) {
                int id = (int) view.getModeloTabela().getValueAt(linha, 0);
                model.excluir(id);
                atualizarTabela();
                view.limparCampos();
                view.exibirAlerta("Produto removido!");
            } else {
                view.exibirAlerta("Selecione um produto para excluir!");
            }
        });

        this.view.acaoEditar(e -> {
            int linha = view.getTabela().getSelectedRow();
            if (linha != -1) {
                try {
                    int id = (int) view.getModeloTabela().getValueAt(linha, 0);
                    String nome = view.getNomeProduto();
                    int qtd = Integer.parseInt(view.getQuantidade());
                    double preco = Double.parseDouble(view.getPreco().replace(",", "."));

                    model.editar(new Produto(id, nome, preco, qtd));
                    atualizarTabela();
                    view.exibirAlerta("Produto atualizado com sucesso!");
                } catch (Exception ex) {
                    view.exibirAlerta("Erro ao editar: Verifique se todos os campos estão preenchidos corretamente.");
                }
            } else {
                view.exibirAlerta("Primeiro, selecione um produto na tabela!");
            }
        });

        this.view.acaoSairAdmin(e -> this.navegador.navegarPara("LOGIN"));
        
        atualizarTabela();
    }

    private void atualizarTabela() {
        view.getModeloTabela().setRowCount(0);
        List<Produto> lista = model.listarTodos();
        for (Produto p : lista) {
            view.getModeloTabela().addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()});
        }
    }
}