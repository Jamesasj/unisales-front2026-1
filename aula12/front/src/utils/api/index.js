const token = localStorage.getItem('API-KEY');
const BASE_URL = 'http://localhost:8080/api';
let header = {
    'Content-Type': 'application/json'
}

async function post(uri, body) {
    const callapi = `${BASE_URL}${uri}`
    if (token) {
        header = { ...header, 'Authorization': `Bearer ${token}` };
    }

    const res = await fetch(callapi, {
        method: 'POST',
        headers: header,
        body: JSON.stringify(body)
    });

    if (res.status > 400 && res.status < 500) {
        alert('nao autorizado')
        return res.status;
    }

    const obj = await res.json();
    return obj.data;
}

async function get(uri) {
    const callapi = `${BASE_URL}${uri}`
    if (token) {
        header = { ...header, 'Authorization': `Bearer ${token}` };
    }
    const res = await fetch(callapi, {
        method: 'GET',
        headers: header,
    });

    if (res.status > 400 && res.status < 500) {
        alert('nao autorizado')
        return res.status;
    }

    const obj = await res.json();

    return obj.data;

}

const api = {
    post, get
}

export default api