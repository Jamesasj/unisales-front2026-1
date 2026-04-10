// vou criar uma chamada ao endpoit de usuarios e vou atualizar a lista do index.html

const lista = fetch('https://jsonplaceholder.typicode.com/users/'); /// asincrono;


lista.then((response)=>{
    return response.json();

}).then((users)=>{
    const tabela = document.getElementById('tabela');
    users.forEach(user => {
        const row = document.createElement('tr');
        const nameCell = document.createElement('td');
        nameCell.textContent = user.name;
        const ageCell = document.createElement('td');
        ageCell.textContent = user.email || 'N/A'; 
        const editButton = document.createElement('button');
        editButton.textContent = 'Editar';
        editButton.setAttribute('data-id', user.id);
        const buttonCell = document.createElement('td');
        buttonCell.appendChild(editButton);
        row.appendChild(nameCell);
        row.appendChild(ageCell);
        row.appendChild(buttonCell);
        tabela.appendChild(row);
    });


})



console.log('executo 1')

console.log(lista)