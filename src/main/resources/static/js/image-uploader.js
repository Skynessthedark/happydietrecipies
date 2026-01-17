(function (window) {

    function ImageUploader(options) {

        const config = {
            uploadAreaId: '',
            previewImgId: '',
            previewDivId: '',
            inputFileId: '',
            removeBtnId: '',
            ...options
        };

        function init() {

            const input = document.getElementById(config.inputFileId);

            input.addEventListener('change', function(e) {
                previewImage();
            });

            const imageUploadArea = document.getElementById(config.uploadAreaId);

            if (imageUploadArea === undefined) {
                console.error('SearchSelector: search input not found');
                return;
            }

            imageUploadArea.addEventListener('click', function(e) {
                e.preventDefault();
                input.addEventListener('change', function(e) {
                    previewImage();
                });
            });

            imageUploadArea.addEventListener('dragover', function(e) {
                e.preventDefault();
                this.classList.add('dragover');
            });

            imageUploadArea.addEventListener('dragleave', function(e) {
                e.preventDefault();
                this.classList.remove('dragover');
            });

            imageUploadArea.addEventListener('drop', function(e) {
                e.preventDefault();
                this.classList.remove('dragover');

                const files = e.dataTransfer.files;
                if (files.length > 0) {
                    const input = this.parentElement.querySelector('input[type="file"]');
                    input.files = files;
                    this.previewImage();
                }
            });

            imageUploadArea.onclick = function (e) {
                e.preventDefault();
                input.click();
            };

            const removeBtn = document.getElementById(config.removeBtnId);
            removeBtn.addEventListener("click", function (e) {
                e.stopPropagation();

                const file = document.getElementById(config.inputFileId).files[0];
                const previewImg = document.getElementById(config.previewImgId);
                const previewDiv = document.getElementById(config.previewDivId);
                const removeBtn = document.getElementById(config.removeBtnId);

                input.value = "";
                previewImg.src = "";
                previewImg.style.display = "none";
                previewDiv.style.display = "block";
                removeBtn.style.display = "none";
            });
        }

        function previewImage() {
            const file = document.getElementById(config.inputFileId).files[0];
            const preview = document.getElementById(config.previewImgId);
            const previewDiv = document.getElementById(config.previewDivId);
            const removeBtn = document.getElementById(config.removeBtnId);

            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                    previewDiv.style.display = 'none';
                    removeBtn.style.display = "block";
                }
                reader.readAsDataURL(file);
            }
        }

        return {
            init,
            previewImage
        };
    }

    window.ImageUploader = ImageUploader;

})(window);




