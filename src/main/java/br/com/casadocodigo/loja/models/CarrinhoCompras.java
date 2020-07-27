package br.com.casadocodigo.loja.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import br.com.casadocodigo.loja.daos.CompraDao;
import br.com.casadocodigo.loja.daos.UsuarioDao;
import br.com.casadocodigo.loja.service.PagamentoGateway;

// Named se torna um Bean do CDI
@Named
@SessionScoped
public class CarrinhoCompras implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Set<CarrinhoItem> itens = new HashSet<>();
	
	@Inject
	private CompraDao compraDao;
	
//	@Inject
//	private PagamentoGateway pagamentoGateway;

    public void add(CarrinhoItem item) {
        itens.add(item);
    }

	public List<CarrinhoItem> getItens() {
		return new ArrayList<CarrinhoItem>(itens);
	}
	
	public BigDecimal getTotal(CarrinhoItem item) {
	    return item.getLivro().getPreco().multiply(new BigDecimal(item.getQuantidade()));
	}
	
	
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (CarrinhoItem carrinhoItem : itens) {
			total = total.add(carrinhoItem.getLivro().getPreco().multiply(new BigDecimal(carrinhoItem.getQuantidade())));
		}
		return total;
		
	}

	public void remover(CarrinhoItem item) {
		this.itens.remove(item);
		
	}
	
	public Integer getQuantidadeTotal() {
		return itens.stream().mapToInt(item -> item.getQuantidade()).sum();
	}

	public void finalizar(Compra compra) {		
		compra.setItens(this.toJson());
		compra.setTotal(getTotal());
		compraDao.salvar(compra);
		
/*		
		// API de pagamento metodo POST - MAIS verbosa
		
		Client client = ClientBuilder.newClient();
		Pagamento pagamento = new Pagamento(getTotal());
		String target = "http://book-payment.herokuapp.com/payment";
		Entity<Pagamento> json = Entity.json(pagamento);
		WebTarget webTarget = client.target(target);
		Builder request = webTarget.request();
		String response = request.post(json, String.class);
		System.out.println(response);

		
		// API de pagamento metodo POST - MENOS verbosa
		
		String response = pagamentoGateway.pagar(getTotal());
		System.out.println(response);
*/
	}



	public String toJson() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (CarrinhoItem item : itens) {
			builder.add(Json.createObjectBuilder().add("titulo", item.getLivro().getTitulo())
					.add("preco", item.getLivro().getPreco())
					.add("quantidade", item.getQuantidade())
					.add("total", getTotal(item)));
		}
		String json =  builder.build().toString();
		System.out.println(json);		
		return json;
	}
	

}
