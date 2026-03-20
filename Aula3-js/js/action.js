/// ao clicar no botao salvar a pessoa deve ser inclui na lista

const salvarNaLista = (pessoa) => {
    function createTd(value) {
        const tdObj = document.createElement('td');
        tdObj.innerHTML = value;
        return tdObj;
    }

    const tbl = document.getElementById('tbl');
    const tblBody = tbl.querySelector('tbody');
    const nElem = document.createElement('tr')
    nElem.appendChild(createTd(pessoa.nome));
    nElem.appendChild(createTd(pessoa.idade));
    nElem.appendChild(createTd(pessoa.endereco));
    
    tblBody.appendChild(nElem);
}

const getPessoa = () => {
    const objNome = document.querySelector('#nome > input');
    const objIdade = document.querySelector('#idade > input');
    const objEnd = document.querySelector('#end > input');

    const pessoa = { nome: objNome.value, idade: objIdade.value, endereco: objEnd.value };
    console.log(pessoa);
    salvarNaLista(pessoa)
}

const btn = document.getElementById("btnSalvar");
btn.onclick = getPessoa;