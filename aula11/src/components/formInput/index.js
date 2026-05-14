import style from './style.css'
const FormInput = (props) => { /// propriedade

    const onChange = (newValue) => {
        props.onChange(newValue.target.value);
    }

    return (
        <div className='form-input'>
            <label>{props.name}</label>
            <input value={props.value} type={props?.type||'input'} onChange={onChange}></input>
        </div>
    )
}

export default FormInput;