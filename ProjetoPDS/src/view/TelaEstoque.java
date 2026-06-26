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
        setLayout(new MigLayout("fill, insets 25", "[][grow][][grow][][grow][]", "[][][grow, fill][]"));

        JLabel lblTitulo = new JLabel("Gerenciamento de Estoque (ADM)");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(lblTitulo, "span 7, center, gapbottom 25, wrap");

        add(new JLabel("Produto:"));
        txtNomeProd = new JTextField();
        add(txtNomeProd, "growx");

        add(new JLabel("Qtd:"), "gapleft 10");
        txtQtd = new JTextField();
        add(txtQtd, "width 60!"); 

        add(new JLabel("Preço:"), "gapleft 10");
        txtPreco = new JTextField();
        add(txtPreco, "growx");

        btnAddProduto = new JButton("Salvar Novo");
        add(btnAddProduto, "wrap, width 120!");

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Estoque"}, 0);
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), "span 7, grow, gapy 15, wrap"); 

        btnRemoverProduto = new JButton("Excluir");
        btnEditarProduto = new JButton("Salvar Edição");
        btnSair = new JButton("Logout");

        add(btnRemoverProduto, "span 7, split 3, left, height 35!"); 
        add(btnEditarProduto, "height 35!");
        add(btnSair, "right, width 100!, height 35!");
    }

    public void setNomeProduto(String n) { txtNomeProd.setText(n); }
    public void setQuantidade(String q) { txtQtd.setText(q); }
    public void setPreco(String p) { txtPreco.setText(p); }
    public void limparCampos() {
        txtNomeProd.setText(""); txtQtd.setText(""); txtPreco.setText("");
    }

    public void acaoAdicionarProduto(ActionListener l) { btnAddProduto.addActionListener(l); }
    public void acaoExcluir(ActionListener l) { btnRemoverProduto.addActionListener(l); }
    public void acaoEditar(ActionListener l) { btnEditarProduto.addActionListener(l); }
    public void acaoSairAdmin(ActionListener l) { btnSair.addActionListener(l); }
    
    public JTable getTabela() { return tabela; }
    public DefaultTableModel getModeloTabela() { return modeloTabela; }
    public String getNomeProduto() { return txtNomeProd.getText(); }
    public String getQuantidade() { return txtQtd.getText(); }
    public String getPreco() { return txtPreco.getText(); }
    public void exibirAlerta(String m) { JOptionPane.showMessageDialog(this, m); }
}