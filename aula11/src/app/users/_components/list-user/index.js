import style from './style.css'

const ListUsers = ({ data }) => {
    return (
        <table className='list-users'>
            <thead class='table-header'>
                <tr>
                    <td>id</td>
                    <td>nome</td>
                    <td>email</td>
                </tr>

            </thead>
            <tbody>
                {data.map((user) => {
                    return (
                        <tr key={user.id} className='row'>
                            <td>{user.id}</td>
                            <td>{user.name}</td>
                            <td>{user.email}</td>
                        </tr>
                    )
                })

                }
            </tbody>
        </table>
    )
}


export default ListUsers;