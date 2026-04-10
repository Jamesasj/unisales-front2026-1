const btn = document.getElementById('create');
btn.onclick = async () => {
    const objname = document.getElementById('name')
    const objemail = document.getElementById('email')

    const nome = objname.value;
    const email = objemail.value;
    const user = { name: nome, email: email };
    /* assincrono
    fetch('https://jsonplaceholder.typicode.com/users/',
        {
            method: 'POST',
            body: JSON.stringify(user)
        }
    ).then((res) => {
        setTimeout(()=>console.log('ola'), 100000);
        if(res.status == 201){
            return res.json();
        }
    }).then((obj) => {
        console.log('galinha')
        alert(`Uusario ${obj.id} criado com sucesso`)
    })
 */

    const res = await fetch('https://jsonplaceholder.typicode.com/users/',
        {
            method: 'POST',
            body: JSON.stringify(user)
        }
    )

    const ret = await res.json();

    if (res.status == 201) {
        console.log('galinha')
        alert(`Usuario ${ret.id} criado com sucesso`)
    }

    console.log('ovo')


}