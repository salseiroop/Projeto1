package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UsuarioDAO {

    public void salvar(Usuario u) {
        String sql = "INSERT INTO usuarios (cpf, nome, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            
            pstm.setString(1, u.getCpf());
            pstm.setString(2, u.getNome());
            pstm.setBoolean(3, u.isIsAdmin());
            
            pstm.executeUpdate();
        } catch (SQLException e) {
            // Trata especificamente o erro de CPF já existente
            if (e.getErrorCode() == 1062) { 
                JOptionPane.showMessageDialog(null, "Este CPF já está cadastrado!");
            } else {
                System.err.println("Erro ao salvar usuário: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Usuario validarLogin(String nome, String cpf) {
        // Busca exata por nome e cpf
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
            System.err.println("Erro no login (SQL): " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Retorna null se não encontrar o par Nome/CPF
    }
}