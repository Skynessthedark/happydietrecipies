(function (window) {

    function SearchSelector(options) {

        const config = {
            searchInputId: '',
            resultContainerId: '',
            selectedContainerId: '',
            hiddenInputId: '',
            searchUrl: '',
            minLength: 3,
            ...options
        };

        let selectedOptions = [];

        function init() {
            const input = document.getElementById(config.searchInputId);

            if (!input) {
                console.error('SearchSelector: search input not found');
                return;
            }

            input.addEventListener('keyup', function (e) {
                if (e.key === 'Enter') {
                    search();
                }
            });
        }

        function search() {
            const text = document.getElementById(config.searchInputId).value.trim();

            if (text.length < config.minLength) {
                alert(`Please enter at least ${config.minLength} letters.`);
                return;
            }

            fetch(`${config.searchUrl}${encodeURIComponent(text)}`)
                .then(res => res.json())
                .then(renderResults);
        }

        function renderResults(items) {
            const container = document.getElementById(config.resultContainerId);
            container.innerHTML = '';

            if (!items || items.length === 0) {
                container.innerHTML = `<div class="list-group-item">No result.</div>`;
                container.style.display = 'block';
                return;
            }

            items.forEach(item => {
                const el = document.createElement('a');
                el.href = '#';
                el.className = 'list-group-item list-group-item-action';
                el.textContent = `${item.code} - ${item.name}`;

                el.onclick = function (e) {
                    e.preventDefault();
                    add(item);
                };

                container.appendChild(el);
            });

            container.style.display = 'block';
        }

        function add(item) {
            if (selectedOptions.some(i => i.code === item.code)) return;

            selectedOptions.push(item);
            renderSelected();
        }

        function remove(code) {
            selectedOptions = selectedOptions.filter(i => i.code !== code);
            renderSelected();
        }

        function renderSelected() {
            const container = document.getElementById(config.selectedContainerId);
            container.innerHTML = '';

            selectedOptions.forEach(item => {
                const badge = document.createElement('span');
                badge.className = 'badge bg-primary me-2 mb-2';
                badge.innerHTML = `
                    ${item.name}
                    <span style="cursor:pointer;margin-left:6px"
                          data-code="${item.code}">✕</span>
                `;

                badge.querySelector('span').onclick = () => remove(item.code);
                container.appendChild(badge);
            });

            document.getElementById(config.hiddenInputId).value =
                selectedOptions.map(i => i.code).join(',');

            const searchContainer = document.getElementById(config.resultContainerId);
            searchContainer.innerHTML = '';
            document.getElementById(config.searchInputId).value = '';
        }

        return {
            init,
            search
        };
    }

    window.SearchSelector = SearchSelector;

})(window);
