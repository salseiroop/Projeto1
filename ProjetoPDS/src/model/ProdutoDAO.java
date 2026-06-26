package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProdutoDAO {

    public String buscarUltimoNome() {
        String sql = "SELECT nome FROM produtos ORDER BY id DESC LIMIT 1";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean salvar(Produto p) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, p.getNome());
            pstm.setDouble(2, p.getPreco());
            pstm.setInt(3, p.getQuantidade());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null,
                    "Erro: Já existe um produto com este nome cadastrado!",
                    "Produto Duplicado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "Erro ao salvar produto no banco de dados.\nDetalhe: " + e.getMessage(),
                    "Erro de Banco", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            return false;
        }
    }

    public void excluir(int id) {
        String sqlDelete      = "DELETE FROM produtos WHERE id = ?";
        String sqlReorg1      = "SET @count = 0";
        String sqlReorg2      = "UPDATE produtos SET id = (@count := @count + 1)";
        String sqlReorg3      = "ALTER TABLE produtos AUTO_INCREMENT = 1";

        try (Connection conn = BancoDeDados.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstm = conn.prepareStatement(sqlDelete)) {
                pstm.setInt(1, id);
                pstm.executeUpdate();
            }
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlReorg1);
                stmt.execute(sqlReorg2);
                stmt.execute(sqlReorg3);
            }
            conn.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao excluir produto.\nDetalhe: " + e.getMessage(),
                "Erro de Banco", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean editar(Produto p) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, p.getNome());
            pstm.setDouble(2, p.getPreco());
            pstm.setInt(3, p.getQuantidade());
            pstm.setInt(4, p.getId());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null,
                    "Erro: Já existe outro produto com este nome!",
                    "Produto Duplicado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "Erro ao editar produto.\nDetalhe: " + e.getMessage(),
                    "Erro de Banco", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            return false;
        }
    }

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY id ASC";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                lista.add(new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao carregar lista de produtos.\nDetalhe: " + e.getMessage(),
                "Erro de Banco", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return lista;
    }
}
