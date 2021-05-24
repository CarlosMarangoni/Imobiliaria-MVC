$(function () {
	$('.js-currency').maskMoney({
		decimal: ',',
		thousands: '.',
		allowZero: true
	});
})

$(document).ready(function () {
	if (window.location.href.indexOf('radioFilter=descricao') > -1) {
		$('#filterDesc').prop('checked', true);
	}
	if (window.location.href.indexOf('radioFilter=quartos') > -1) { //Mantém o radioButton marcado de acordo com a URL
		$('#filterQuartos').prop('checked', true);
	}
	if (window.location.href.indexOf('radioFilter=categoria') > -1) {
		$('#filterCategoria').prop('checked', true);
	}
	if (window.location.href.indexOf('radioFilter=negocio') > -1) {
		$('#filterNegocio').prop('checked', true);
	}
	if (window.location.href.indexOf('radioFilter=estado') > -1) { //Mantém o radioButton marcado de acordo com a URL
		$('#filterEstado').prop('checked', true);
	}
	if (window.location.href.indexOf('radioFilter=municipio') > -1) {
		$('#filterMunicipio').prop('checked', true);
	}
	if (window.location.href.indexOf('radioFilter=nome') > -1) {
		$('#filterNome').prop('checked', true);
	}
	if (window.location.href.indexOf('radioFilter=nome') > -1) {
		$('#filterMunicipio').prop('checked', true);
	}

});

$("#estado").change(function () {
	let municipio = $('#municipio');
	municipio.empty();

	let url;

	if (window.location.href.indexOf('bairros/') > -1) {
		url = '/bairros/listarMunicipioPorEstado?codigoEstado=' + $("#estado").val();
	}
	if (window.location.href.indexOf('imoveis/') > -1) {
		url = '/imoveis/listarMunicipioPorEstado?codigoEstado=' + $("#estado").val();
		const bairro = $('#bairro');
		bairro.empty();
	}
	$.getJSON(url, function (data) {														//Requisição Ajax para preencher os Selects com base no select anterior
		$.each(data, function (key, objeto) {											
			municipio.append($('<option></option').attr('value', objeto.codigo).text(objeto.nome));
		})
	})

});

$("#municipio").change(function () {
	let bairro = $('#bairro');
	bairro.empty();

	const url = '/imoveis/listarBairroPorMunicipio?codigoMunicipio=' + $("#municipio").val();

	$.getJSON(url, function (data) {
		$.each(data, function (key, objeto) {												//Requisição Ajax para preencher os Selects com base no select anterior
			bairro.append($('<option></option').attr('value', objeto.codigo).text(objeto.nome));
		})
	})

});


$(document).ready(function () {

	$('#fileImage').change(function () {
		showImageThumbnail(this);			//Mostrar imagem selecionada ao cadastrar imóvel
	});

});


//Mostrar imagem selecionada ao cadastrar imóvel
function showImageThumbnail(fileInput) {
	file = fileInput.files[0];
	reader = new FileReader();

	reader.onload = function (e) {
		$('#thumbnail').attr('src', e.target.result)
	};

	reader.readAsDataURL(file);
}

//Modal CATEGORIA
$(confirmacaoExclusaoModalCateg).on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget);

	var codigoCategoria = button.data('codigo');

	var modal = $(this);
	var nomeCategoria = button.data('nome');
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';

	}
	form.attr('action', action + codigoCategoria);

	modal.find('.modal-body span').html('Tem certeza que deseja excluir a categoria <strong>' + nomeCategoria + '</strong>?');

});

//Modal NEGOCIOS

$(confirmacaoExclusaoModalNegoc).on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget);

	var codigoNegocio = button.data('codigo');

	var modal = $(this);
	var nomeNegocio = button.data('nome');
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';

	}
	form.attr('action', action + codigoNegocio);

	modal.find('.modal-body span').html('Tem certeza que deseja excluir o negócio <strong>' + nomeNegocio + '</strong>?');

});
//Modal ESTADOS
$(confirmacaoExclusaoModalEstad).on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget);

	var codigoEstado = button.data('codigo');

	var modal = $(this);
	var nomeEstado = button.data('nome');
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';

	}
	form.attr('action', action + codigoEstado);

	modal.find('.modal-body span').html('Tem certeza que deseja excluir o estado <strong>' + nomeEstado + '</strong>?');

});


//Modal MUNICIPIOS
$(confirmacaoExclusaoModalMunic).on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget);

	var codigoMunic = button.data('codigo');

	var modal = $(this);
	var nomeMunicipio = button.data('nome');
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';

	}
	form.attr('action', action + codigoMunic);

	modal.find('.modal-body span').html('Tem certeza que deseja excluir o município <strong>' + nomeMunicipio + '</strong>?');

});

//Modal BAIRROS
$(confirmacaoExclusaoModalBairro).on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget);

	var codigoBairro = button.data('codigo');

	var modal = $(this);
	var nomeBairro = button.data('nome');
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';

	}
	form.attr('action', action + codigoBairro);

	modal.find('.modal-body span').html('Tem certeza que deseja excluir o bairro <strong>' + nomeBairro + '</strong>?');

});

//Modal IMOVEIS
$(confirmacaoExclusaoModalImovel).on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget);

	var codigoImovel = button.data('codigo');

	var modal = $(this);
	var descImovel = button.data('nome');
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';

	}
	form.attr('action', action + codigoImovel);

	modal.find('.modal-body span').html('Tem certeza que deseja excluir o imóvel <strong>' + descImovel + '</strong>?');

});

//Modal USUARIOS
$(confirmacaoExclusaoModalUsers).on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget);

	var codigoUsuario = button.data('codigo');

	var modal = $(this);
	var emailUsuario = button.data('nome');
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';

	}
	form.attr('action', action + codigoUsuario);

	modal.find('.modal-body span').html('Tem certeza que deseja excluir o imóvel <strong>' + emailUsuario + '</strong>?');

});

