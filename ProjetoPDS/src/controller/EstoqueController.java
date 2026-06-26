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
                    String nome     = view.getModeloTabela().getValueAt(linha, 1).toString();
                    String precoTab = view.getModeloTabela().getValueAt(linha, 2).toString();
                    String qtd      = view.getModeloTabela().getValueAt(linha, 3).toString();
                    String precoLimpo = precoTab.replace("R$", "").replace(",", ".").trim();
                    view.setNomeProduto(nome);
                    view.setPreco(precoLimpo);
                    view.setQuantidade(qtd);
                }
            }
        });

        this.view.acaoAdicionarProduto(e -> {
            String nomeNovo = view.getNomeProduto().trim();
            if (nomeNovo.isEmpty()) {
                view.exibirAlerta("O nome do produto não pode estar vazio!");
                return;
            }
            if (nomeNovo.length() > 100) {
                view.exibirAlerta("O nome do produto deve ter no máximo 100 caracteres!");
                return;
            }
            if (nomeNovo.matches("\\d+")) {
                view.exibirAlerta("O nome do produto não pode ser somente números!");
                return;
            }

            String qtdStr   = view.getQuantidade().trim();
            String precoStr = view.getPreco().replace("R$", "").replace(",", ".").trim();

            if (qtdStr.isEmpty()) {
                view.exibirAlerta("O campo Quantidade não pode estar vazio!");
                return;
            }
            if (precoStr.isEmpty()) {
                view.exibirAlerta("O campo Preço não pode estar vazio!");
                return;
            }

            int qtd;
            try {
                qtd = Integer.parseInt(qtdStr);
            } catch (NumberFormatException ex) {
                view.exibirAlerta("Quantidade inválida! Digite apenas números inteiros.");
                return;
            }

            if (qtd < 0) {
                view.exibirAlerta("A quantidade não pode ser negativa!");
                return;
            }

            double preco;
            try {
                preco = Double.parseDouble(precoStr);
            } catch (NumberFormatException ex) {
                view.exibirAlerta("Preço inválido! Use apenas números (ex: 9.99).");
                return;
            }

            if (preco < 0) {
                view.exibirAlerta("O preço não pode ser negativo!");
                return;
            }

            String ultimoNome = model.buscarUltimoNome();
            if (nomeNovo.equalsIgnoreCase(ultimoNome)) {
                view.exibirAlerta("Erro: Este produto é idêntico ao último cadastrado!");
                return;
            }

            if (model.salvar(new Produto(nomeNovo, preco, qtd))) {
                view.exibirAlerta("Produto salvo com sucesso!");
                view.limparCampos();
                atualizarTabela();
            }
        });

        this.view.acaoExcluir(e -> {
            int linha = view.getTabela().getSelectedRow();
            if (linha != -1) {
                int id = (int) view.getModeloTabela().getValueAt(linha, 0);
                model.excluir(id);
                atualizarTabela();
                view.limparCampos();
                view.exibirAlerta("Produto removido com sucesso!");
            } else {
                view.exibirAlerta("Selecione um produto na tabela para excluir!");
            }
        });

        this.view.acaoEditar(e -> {
            int linha = view.getTabela().getSelectedRow();
            if (linha == -1) {
                view.exibirAlerta("Primeiro, selecione um produto na tabela!");
                return;
            }

            String nome     = view.getNomeProduto().trim();
            String qtdStr   = view.getQuantidade().trim();
            String precoStr = view.getPreco().replace("R$", "").replace(",", ".").trim();

            if (nome.isEmpty()) {
                view.exibirAlerta("O nome do produto não pode estar vazio!");
                return;
            }
            if (nome.length() > 100) {
                view.exibirAlerta("O nome do produto deve ter no máximo 100 caracteres!");
                return;
            }
            if (nome.matches("\\d+")) {
                view.exibirAlerta("O nome do produto não pode ser somente números!");
                return;
            }
            if (qtdStr.isEmpty()) {
                view.exibirAlerta("O campo Quantidade não pode estar vazio!");
                return;
            }
            if (precoStr.isEmpty()) {
                view.exibirAlerta("O campo Preço não pode estar vazio!");
                return;
            }

            int qtd;
            try {
                qtd = Integer.parseInt(qtdStr);
            } catch (NumberFormatException ex) {
                view.exibirAlerta("Quantidade inválida! Digite apenas números inteiros.");
                return;
            }

            if (qtd < 0) {
                view.exibirAlerta("A quantidade não pode ser negativa!");
                return;
            }

            double preco;
            try {
                preco = Double.parseDouble(precoStr);
            } catch (NumberFormatException ex) {
                view.exibirAlerta("Preço inválido! Use apenas números (ex: 9.99).");
                return;
            }

            if (preco < 0) {
                view.exibirAlerta("O preço não pode ser negativo!");
                return;
            }

            int id = (int) view.getModeloTabela().getValueAt(linha, 0);
            if (model.editar(new Produto(id, nome, preco, qtd))) {
                atualizarTabela();
                view.exibirAlerta("Produto atualizado com sucesso!");
                view.limparCampos();
                view.getTabela().clearSelection();
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