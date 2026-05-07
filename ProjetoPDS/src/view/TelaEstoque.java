package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class TelaEstoque extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtNomeProd, txtQtd, txtPreco;
    private JButton btnAddProduto, btnRemoverProduto, btnEditarProduto, btnSair;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaEstoque() {
        setLayout(new MigLayout("fillx, insets 25", "[][grow][][grow][][grow][]", "[][][grow][]"));

        JLabel lblTitulo = new JLabel("Gerenciamento de Estoque (ADM)");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(lblTitulo, "span 7, center, gapbottom 25, wrap");

        add(new JLabel("Produto:"), "gapleft 10");
        txtNomeProd = new JTextField();
        add(txtNomeProd, "growx, width 120:180:250");

        add(new JLabel("Qtd:"), "gapleft 10");
        txtQtd = new JTextField();
        add(txtQtd, "width 50!"); 

        add(new JLabel("Preço:"), "gapleft 10");
        txtPreco = new JTextField();
        add(txtPreco, "growx, width 80:100:150");

        btnAddProduto = new JButton("Salvar Novo");
        add(btnAddProduto, "wrap, gapleft 10");

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Estoque"}, 0);
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), "span 7, grow, gapy 15, wrap"); 

        btnRemoverProduto = new JButton("Excluir Selecionado");
        add(btnRemoverProduto, "span 7, split 3, left"); 
        
        btnEditarProduto = new JButton("Confirmar Edição");
        add(btnEditarProduto);

        btnSair = new JButton("Logout");
        add(btnSair, "right");
    }

    public void setNomeProduto(String nome) { txtNomeProd.setText(nome); }
    public void setQuantidade(String qtd) { txtQtd.setText(qtd); }
    public void setPreco(String preco) { txtPreco.setText(preco); }
    
    public void limparCampos() {
        txtNomeProd.setText("");
        txtQtd.setText("");
        txtPreco.setText("");
    }

    public void acaoAdicionarProduto(ActionListener listener) { btnAddProduto.addActionListener(listener); }
    public void acaoExcluir(ActionListener listener) { btnRemoverProduto.addActionListener(listener); }
    public void acaoEditar(ActionListener listener) { btnEditarProduto.addActionListener(listener); }
    public void acaoSairAdmin(ActionListener listener) { btnSair.addActionListener(listener); }
    
    public JTable getTabela() { return tabela; }
    public DefaultTableModel getModeloTabela() { return modeloTabela; }
    public String getNomeProduto() { return txtNomeProd.getText(); }
    public String getQuantidade() { return txtQtd.getText(); }
    public String getPreco() { return txtPreco.getText(); }

    public void exibirAlerta(String mensagem) { JOptionPane.showMessageDialog(this, mensagem); }
}