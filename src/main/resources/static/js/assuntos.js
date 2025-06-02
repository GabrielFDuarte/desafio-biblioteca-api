    const apiUrl = 'http://localhost:8080/api/assuntos';

    const notyf = new Notyf({
                duration: 3000,
                position: {x: 'right', y: 'top'},
                types: [
                    {type: 'error', background: 'indianred', icon: false},
                    {type: 'success', background: 'seagreen', icon: false}
                ]
    });

    async function listarAssuntos() {
        const res = await fetch(apiUrl);
        const assuntos = await res.json();
        const lista = document.getElementById('listaAssuntos');
        lista.innerHTML = '';
        assuntos.forEach(assunto => {
            const li = document.createElement('li');
            li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
            li.innerHTML = `
                ${assunto.codAs} - ${assunto.descricao}
                <button class="btn btn-danger btn-sm" onclick="excluirAssunto(${assunto.codAs})">Excluir</button>
            `;
            lista.appendChild(li);
        });
    }

    async function excluirAssunto(id) {
        if (!confirm(`Tem certeza que deseja excluir o assunto com ID ${id}?`)) return;

        const res = await fetch(`${apiUrl}/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            exibirToastSuccess("Assunto excluído com sucesso!");
            listarAssuntos();
        } else {
            exibirToastError("Erro ao excluir assunto.");
        }
    }

    document.getElementById('formCadastrarAssunto').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const data = { descricao: form.descricao.value };
        const res = await fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (res.ok) exibirToastSuccess("Assunto cadastrado com sucesso!");
        else exibirToastError("Erro ao cadastrar assunto.");
        form.reset();
        listarAssuntos();
    });

    document.getElementById('formBuscarAssunto').addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = e.target.id.value;
        const res = await fetch(`${apiUrl}/${id}`);
        const detalhes = document.getElementById('detalhesAssunto');
        if (res.ok) {
            const assunto = await res.json();
            detalhes.innerHTML = `
                <h5>${assunto.descricao}</h5>
                <p><strong>ID:</strong> ${assunto.codAs}</p>
            `;
        } else {
            detalhes.innerHTML = '<p class="text-danger">Assunto não encontrado.</p>';
        }
    });

    document.getElementById('formBuscarDescricao').addEventListener('submit', async (e) => {
        e.preventDefault();
        const descricao = e.target.descricao.value;
        const res = await fetch(`${apiUrl}/buscar?descricao=${encodeURIComponent(descricao)}`);
        const lista = document.getElementById('resultadoBuscaDescricao');
        lista.innerHTML = '';

        if (res.ok) {
            const assuntos = await res.json();
            if (assuntos.length === 0) {
                lista.innerHTML = '<li class="list-group-item text-muted">Nenhum assunto encontrado.</li>';
            } else {
                assuntos.forEach(assunto => {
                    const li = document.createElement('li');
                    li.classList.add('list-group-item');
                    li.textContent = `${assunto.codAs} - ${assunto.descricao}`;
                    lista.appendChild(li);
                });
            }
        } else {
            lista.innerHTML = '<li class="list-group-item text-danger">Erro ao buscar assuntos.</li>';
        }
    });

    document.getElementById('formEditarAssunto').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const id = form.id.value;
        const novaDescricao = form.descricao.value;

        const res = await fetch(`${apiUrl}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ descricao: novaDescricao })
        });

        if (res.ok) {
            exibirToastSuccess("Assunto editado com sucesso!");
            form.reset();
            listarAssuntos();
        } else {
            exibirToastError("Erro ao editar assunto.");
        }
    });

    function exibirToastError(mensagem) {
        notyf.error(mensagem);
    }

    function exibirToastSuccess(mensagem) {
        notyf.success(mensagem);
    }