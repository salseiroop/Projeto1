package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UsuarioDAO {

    // Alterado de void para boolean para podermos saber se salvou de verdade
    public boolean salvar(Usuario u) {
        String sql = "INSERT INTO usuarios (cpf, nome, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            
            pstm.setString(1, u.getCpf());
            pstm.setString(2, u.getNome());
            pstm.setBoolean(3, u.isIsAdmin());
            
            pstm.executeUpdate();
            return true; // Retorna true se a inserção funcionou
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { 
                JOptionPane.showMessageDialog(null, "Este CPF já está cadastrado!");
            } else {
                System.err.println("Erro ao salvar usuário: " + e.getMessage());
                e.printStackTrace();
            }
            return false; // Retorna false se houve erro (como CPF duplicado)
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
            System.err.println("Erro no login (SQL): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}