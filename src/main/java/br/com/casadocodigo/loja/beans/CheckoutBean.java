package br.com.casadocodigo.loja.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.Usuario;

@Model
public class CheckoutBean {

	
	private Usuario usuario = new Usuario();	
	
	@Inject
	private CarrinhoCompras carrinho;
	
	// Serve para toda vez que haver alteração no banco de dados.
	@Transactional
	public void finalizar() {
		carrinho.finalizar(usuario);		
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}
