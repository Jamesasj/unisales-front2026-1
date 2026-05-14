'use client'

import Image from "next/image";
import styles from "./page.module.css";
import { useState } from "react";

// funcao
export default function Home() {
  const [accont, setAccount] = useState({user:"", pass:""})
  

  const action = ()=> {
    console.log(accont)
  }
  const onChangeUser = (e)=>{
    setAccount({...accont , user: e.target.value});
  }
  const onChangePass = (e)=>{
    setAccount({...accont , pass:e.target.value});
  }

  return (
    <>
      <h1>Titulo da pagina</h1>
      <h2>Meu subtitulo</h2>
      <input placeholder="usuario" value={accont.user} onChange={onChangeUser} />
      <input type="password" placeholder="senha" value={accont.pass} onChange={onChangePass} />
      <button onClick={action}>Entrar</button>
    </>
  );
}
