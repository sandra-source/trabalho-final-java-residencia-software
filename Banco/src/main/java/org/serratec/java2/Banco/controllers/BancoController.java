package org.serratec.java2.Banco.controllers;

import org.serratec.java2.Banco.dominio.Conta;
import org.serratec.java2.Banco.dominio.Tipo;
import org.serratec.java2.Banco.exceptions.ContaInvalidaException;
import org.serratec.java2.Banco.exceptions.ContaNotFoundException;
import org.serratec.java2.Banco.exceptions.SaldoInsuficienteException;
import org.serratec.java2.Banco.services.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/conta" })
public class BancoController {
	
	@Autowired
	private BancoService bancoService;

	@GetMapping
	public ResponseEntity<?> getContas() {
		HttpHeaders cabecalho = new HttpHeaders();
		cabecalho.add("RESIDENCIA-SOFTWARE ", "2020");
		return new ResponseEntity(bancoService.listarContas(), cabecalho, HttpStatus.OK);
	}
	
	@GetMapping("/{numero}")
	public ResponseEntity<?> getConta(@PathVariable Integer numero) throws ContaNotFoundException, ContaInvalidaException {

		Conta conta = bancoService.recuperarPorNumero(numero);

		return ResponseEntity.status(HttpStatus.OK).body(conta);

	}
	
	@PostMapping
	public ResponseEntity<?> incluir(@RequestBody Conta novaConta) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bancoService.incluir(novaConta));
	}
	
	@PutMapping
	public ResponseEntity<?> atualizar(@RequestBody Conta conta) throws ContaNotFoundException, ContaInvalidaException {

		Conta contaAtualizada = bancoService.atualizarPorNumero(conta);
		return ResponseEntity.status(HttpStatus.OK).body(contaAtualizada);
	}
	

	@DeleteMapping("/{numero}")
	public ResponseEntity<?> apagar(@PathVariable Integer numero) throws ContaNotFoundException, ContaInvalidaException {

		bancoService.apagarPorNumero(numero);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("/{numero}/{operacao}")
	public ResponseEntity<?> opDebito(@RequestParam Integer numero, Tipo tipo, double valor) throws SaldoInsuficienteException, ContaNotFoundException, ContaInvalidaException{
		return ResponseEntity.status(HttpStatus.CREATED).body(bancoService.operacaoConta(numero, tipo, valor));
	}
}
