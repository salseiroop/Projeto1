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
                    String precoTabela = view.getModeloTabela().getValueAt(linha, 2).toString();
                    String qtd = view.getModeloTabela().getValueAt(linha, 3).toString();
                    String precoLimpo = precoTabela.replace("R$", "").replace(",", ".").trim();
                    view.setNomeProduto(nome);
                    view.setPreco(precoLimpo);
                    view.setQuantidade(qtd);
                }
            }
        });

        this.view.acaoAdicionarProduto(e -> {
            try {
                String nomeNovo = view.getNomeProduto().trim();
                if(nomeNovo.isEmpty()) { view.exibirAlerta("O nome não pode estar vazio!"); return; }
                
                String ultimoNome = model.buscarUltimoNome();
                if (nomeNovo.equalsIgnoreCase(ultimoNome)) {
                    view.exibirAlerta("Erro: Este produto é idêntico ao último cadastrado!");
                    return;
                }
                
                int qtd = Integer.parseInt(view.getQuantidade());
                double preco = Double.parseDouble(view.getPreco().replace("R$", "").replace(",", ".").trim());

                if (model.salvar(new Produto(nomeNovo, preco, qtd))) {
                    view.exibirAlerta("Produto salvo com sucesso!");
                    view.limparCampos();
                    atualizarTabela();
                }
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
                    double preco = Double.parseDouble(view.getPreco().replace("R$", "").replace(",", ".").trim());

                    if (model.editar(new Produto(id, nome, preco, qtd))) {
                        atualizarTabela();
                        view.exibirAlerta("Produto atualizado com sucesso!");
                        view.limparCampos();
                        view.getTabela().clearSelection();
                    }
                } catch (Exception ex) {
                    view.exibirAlerta("Erro ao editar: Verifique os campos.");
                }
            } else {
                view.exibirAlerta("Primeiro, selecione um produto na tabela!");
            }
        });

        this.view.acaoSairAdmin(e -> this.navegador.navegarPara("LOGIN"));
        atualizarTabela();
    }

    public void atualizarTabela() {
        view.getModeloTabela().setRowCount(0);
        List<Produto> lista = model.listarTodos();
        for (Produto p : lista) {
            String precoFormatado = String.format("R$ %.2f", p.getPreco());
            view.getModeloTabela().addRow(new Object[]{p.getId(), p.getNome(), precoFormatado, p.getQuantidade()});
        }
    }
}