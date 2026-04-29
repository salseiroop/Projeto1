package model;

public class Usuario {
    private String cpf;
    private String nome;
    private boolean isAdmin;

    public Usuario(String cpf, String nome, boolean isAdmin) {
        this.cpf = cpf;
        this.nome = nome;
        this.isAdmin = isAdmin;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}