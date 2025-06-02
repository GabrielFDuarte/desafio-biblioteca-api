    const apiUrl = 'http://localhost:8080/api/autores';

    const notyf = new Notyf({
                    duration: 3000,
                    position: {x: 'right', y: 'top'},
                    types: [
                        {type: 'error', background: 'indianred', icon: false},
                        {type: 'success', background: 'seagreen', icon: false}
                    ]
    });

    async function listarAutores() {
        const res = await fetch(apiUrl);
        const autores = await res.json();
        const lista = document.getElementById('listaAutores');

        lista.innerHTML = '';
        autores.forEach(autor => {
            const li = document.createElement('li');
            li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
            li.innerHTML = `
                <span>${autor.codAu} - ${autor.nome}</span>
                <button class="btn btn-danger btn-sm" onclick="excluirAutor(${autor.codAu})">Excluir</button>
            `;
            lista.appendChild(li);
        });
    }

    async function excluirAutor(id) {
        if (confirm(`Tem certeza que deseja excluir o autor com ID ${id}?`)) {
            const res = await fetch(`${apiUrl}/${id}`, {
                method: 'DELETE'
            });

            if (res.ok) {
                exibirToastSuccess("Autor excluído com sucesso!");
                listarAutores();
            } else {
                exibirToastError("Erro ao excluir autor.");
            }
        }
    }

    document.getElementById('formCadastrarAutor').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const data = { nome: form.nome.value };
        const res = await fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (res.ok) {
            exibirToastSuccess("Autor cadastrado com sucesso!");
            form.reset();
            listarAutores();
        } else {
            exibirToastError("Erro ao cadastrar autor.");
        }
    });

    document.getElementById('formBuscarAutor').addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = e.target.id.value;
        const res = await fetch(`${apiUrl}/${id}`);
        const detalhesDiv = document.getElementById('detalhesAutor');
        if (res.ok) {
            const autor = await res.json();
            detalhesDiv.innerHTML = `
                <h5>${autor.nome}</h5>
                <p><strong>ID:</strong> ${autor.codAu}</p>
            `;
        } else {
            detalhesDiv.innerHTML = '<p class="text-danger">Autor não encontrado.</p>';
        }
    });

    document.getElementById('formBuscarPorNome').addEventListener('submit', async (e) => {
        e.preventDefault();
        const nome = e.target.nome.value;
        const res = await fetch(`${apiUrl}/buscar?nome=${encodeURIComponent(nome)}`);
        const resultadoLista = document.getElementById('resultadoBuscaPorNome');
        resultadoLista.innerHTML = '';

        if (res.ok) {
            const autores = await res.json();
            if (autores.length === 0) {
                resultadoLista.innerHTML = '<li class="list-group-item text-danger">Nenhum autor encontrado.</li>';
            } else {
                autores.forEach(autor => {
                    const li = document.createElement('li');
                    li.classList.add('list-group-item');
                    li.textContent = `${autor.codAu} - ${autor.nome}`;
                    resultadoLista.appendChild(li);
                });
            }
        } else {
            resultadoLista.innerHTML = '<li class="list-group-item text-danger">Erro ao buscar autor por nome.</li>';
        }
    });

    document.getElementById('formEditarAutor').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const id = form.id.value;
        const nome = form.nome.value;

        const res = await fetch(`${apiUrl}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome })
        });

        if (res.ok) {
            exibirToastSuccess("Autor atualizado com sucesso!");
            form.reset();
            listarAutores();
        } else {
            exibirToastError("Erro ao atualizar autor.");
        }
    });

    function exibirToastError(mensagem) {
        notyf.error(mensagem);
    }

    function exibirToastSuccess(mensagem) {
        notyf.success(mensagem);
    }