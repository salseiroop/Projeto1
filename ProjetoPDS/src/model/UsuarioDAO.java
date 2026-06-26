package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UsuarioDAO {

    public boolean salvar(Usuario u) {
        String sql = "INSERT INTO usuarios (cpf, nome, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, u.getCpf());
            pstm.setString(2, u.getNome());
            pstm.setBoolean(3, u.isIsAdmin());
            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null,
                    "Erro: Este CPF já está cadastrado no sistema!",
                    "CPF Duplicado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "Erro ao cadastrar usuário.\nDetalhe: " + e.getMessage(),
                    "Erro de Banco", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            return false;
        }
    }

    public Usuario validarLogin(String nome, String cpf) {
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND cpf = ?";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, nome);
            pstm.setString(2, cpf);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getBoolean("is_admin")
                    );
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao tentar realizar login.\nDetalhe: " + e.getMessage(),
                "Erro de Banco", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }
}
