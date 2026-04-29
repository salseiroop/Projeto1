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

        // Cadastrar/Salvar Produto
        this.view.acaoAdicionarProduto(e -> {
            try {
                String nome = view.getNomeProduto();
                int qtd = Integer.parseInt(view.getQuantidade());
                double preco = Double.parseDouble(view.getPreco().replace(",", "."));

                Produto p = new Produto(nome, preco, qtd);
                this.model.salvar(p);
                
                view.exibirAlerta("Produto salvo com sucesso!");
                view.limparCampos();
                atualizarTabela();
            } catch (Exception ex) {
                view.exibirAlerta("Erro nos dados: Verifique valores e quantidades.");
            }
        });

        // AÇÃO: Excluir Selecionado
        this.view.acaoExcluir(e -> {
            int linha = view.getTabela().getSelectedRow();
            if (linha != -1) {
                int id = (int) view.getModeloTabela().getValueAt(linha, 0);
                model.excluir(id);
                atualizarTabela();
                view.exibirAlerta("Produto removido!");
            } else {
                view.exibirAlerta("Selecione um produto na tabela!");
            }
        });

        // AÇÃO: Editar Selecionado
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
                    view.exibirAlerta("Produto editado!");
                } catch (Exception ex) {
                    view.exibirAlerta("Preencha os campos corretamente para editar.");
                }
            } else {
                view.exibirAlerta("Selecione um produto para editar!");
            }
        });

        // Logout conforme requisito 5
        this.view.acaoSairAdmin(e -> {
            this.navegador.navegarPara("LOGIN");
        });
        
        atualizarTabela(); // Carrega os dados ao abrir
    }

    private void atualizarTabela() {
        view.getModeloTabela().setRowCount(0);
        List<Produto> lista = model.listarTodos();
        for (Produto p : lista) {
            view.getModeloTabela().addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()});
        }
    }
}