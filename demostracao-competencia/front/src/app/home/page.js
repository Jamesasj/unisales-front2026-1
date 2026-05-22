'use client'
import Button from "@/components/Button";
import api from "@/utils/api"
export default function HomePage() {
    const listaUsuarios = () => {
        api.get('/users').then((res) => {
            console.log(res)
        })
    }

    return (<>
        <h1>Home</h1>
        <Button type='button' onClick={listaUsuarios}>ObterUsuarios</Button>
    </>)
}