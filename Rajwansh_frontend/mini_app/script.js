let products = [
  { id: 1, name: "Laptop", price: 55000, stock: 5, category: "electronics" },
  { id: 2, name: "Smartphone", price: 20000, stock: 10, category: "electronics" },
  { id: 3, name: "Headphones", price: 2000, stock: 2, category: "accessories" },
  { id: 4, name: "Smart Watch", price: 5000, stock: 0, category: "electronics" },
  { id: 5, name: "Gaming Mouse", price: 1500, stock: 12, category: "accessories" },
  { id: 6, name: "Keyboard", price: 2500, stock: 4, category: "electronics" },
  { id: 7, name: "T-Shirt", price: 800, stock: 15, category: "clothing" },
  { id: 8, name: "Jeans", price: 1500, stock: 0, category: "clothing" },
  { id: 9, name: "Jacket", price: 3000, stock: 6, category: "clothing" },
  { id: 10, name: "Book - JavaScript Guide", price: 500, stock: 8, category: "books" },
  { id: 11, name: "Book - HTML & CSS", price: 400, stock: 3, category: "books" },
  { id: 12, name: "Backpack", price: 1200, stock: 3, category: "accessories" },
  { id: 13, name: "Tablet", price: 30000, stock: 4, category: "electronics" },
  { id: 14, name: "Shoes", price: 2200, stock: 7, category: "clothing" }
];

let filterProducts = [...products];
let currentPage = 1;
const itemsPerPage = 6;

function saveToStorage() {
  localStorage.setItem("products", JSON.stringify(products));
}

function loadFromStorage() {
  const data = localStorage.getItem("products");
  if (data) {
    products = JSON.parse(data);
  }
}

function renderAll() {
  const paginatedData = paginateData(filterProducts, currentPage);

  renderProducts(paginatedData);
  renderPagination(filterProducts);
  updateAnalytics(filterProducts);
}

const grid = document.getElementById("productsGrid");
const loading = document.getElementById("loading");

const totalProductsEl = document.getElementById("totalProducts");
const totalValueEl = document.getElementById("totalValue");
const outOfStockEl = document.getElementById("outOfStock");
const searchInput = document.getElementById("searchInput");
const categoryFilter = document.getElementById("categoryFilter");

function renderProducts(list) {
  grid.innerHTML = "";

  if (list.length === 0) {
    grid.innerHTML = "<p>No products found</p>";
    return;
  }

  list.forEach((product) => {
    const card = document.createElement("div");
    card.classList.add("product-card");

    card.innerHTML = `
      <h3>${product.name}</h3>
      <p>Category: ${product.category}</p>
      <p>Price: ₹${product.price}</p>
      <p>Stock: ${product.stock}</p>
     <div class="action-buttons">
      <button class="edit-btn" onclick="editProduct(${product.id})">Edit</button>
      <button class="delete-btn" onclick="deleteProduct(${product.id})">Delete</button>
    </div>
    `;

    grid.appendChild(card);
  });
}

function paginateData(data, page) {
  const start = (page - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return data.slice(start, end);
}

function renderPagination(data) {
  const pagination = document.getElementById("pagination");
  pagination.innerHTML = "";

  const totalPages = Math.ceil(data.length / itemsPerPage);

  const prev = document.createElement("button");
  prev.innerText = "Prev";
  prev.disabled = currentPage === 1;

  prev.onclick = () => {
    if (currentPage > 1) {
      currentPage--;
      renderAll();
    }
  };

  pagination.appendChild(prev);

  for (let i = 1; i <= totalPages; i++) {
    const btn = document.createElement("button");
    btn.innerText = i;

    if (i === currentPage) {
      btn.style.background = "#333";
      btn.style.color = "white";
    }

    btn.onclick = () => {
      currentPage = i;
      renderAll();
    };

    pagination.appendChild(btn);
  }

  const next = document.createElement("button");
  next.innerText = "Next";
  next.disabled = currentPage === totalPages;

  next.onclick = () => {
    if (currentPage < totalPages) {
      currentPage++;
      renderAll();
    }
  };

  pagination.appendChild(next);
}
function updateAnalytics(list) {

  // Total Products should be the length of the products array
  totalProductsEl.textContent = list.length;

  // Total Inventory Value (price × stock)
  const totalValue = list.reduce((sum, product) => {
    return sum + (product.price * product.stock);
  }, 0);

  totalValueEl.textContent = totalValue;

  //  Out of Stock Products
  const outOfStock = list.filter(product => product.stock === 0).length;

  outOfStockEl.textContent = outOfStock;
}

// Delete product by id
function deleteProduct(id) {
  products = products.filter(p => p.id !== id);

  saveToStorage();
 renderAll();
}

const modal = document.getElementById("productModal");
const addBtn = document.getElementById("addProductBtn");
const closeModalBtn = document.getElementById("closeModalBtn");

addBtn.addEventListener("click", () => {
  modal.style.display = "flex";
});

closeModalBtn.addEventListener("click", () => {
  modal.style.display = "none";
});

function sortProductPrice(type) {

  if (type === "price-low") {
    filterProducts.sort((a, b) => a.price - b.price);
  }

  else if (type === "price-high") {
    filterProducts.sort((a, b) => b.price - a.price);
  }

  else if (type === "Atoz") {
    filterProducts.sort((a, b) => a.name.localeCompare(b.name));
  }

  else if (type === "ZtoA") {
    filterProducts.sort((a, b) => b.name.localeCompare(a.name));
  }

  currentPage = 1;

  renderAll();
}
const sorts = document.getElementById("sortFilter");

sorts.addEventListener("change", (e) => {
  sortProductPrice(e.target.value);
});


function searchProducts(query) {

  query = query.toLowerCase().trim();

  if (query === "") {
    filterProducts = [...products];
  } else {
    filterProducts = products.filter(product =>
      product.name.toLowerCase().includes(query)
    );
  }

  currentPage = 1;

  renderAll();
}
searchInput.addEventListener("input", (e) => {
  searchProducts(e.target.value);
});


function loadCategories() {
  categoryFilter.innerHTML = `<option value="">All Categories</option>`; 

  const categories = [...new Set(products.map(p => p.category))];

  categories.forEach(cat => {
    const option = document.createElement("option");
    option.value = cat;
    option.textContent = cat.charAt(0).toUpperCase() + cat.slice(1);
    categoryFilter.appendChild(option);
  });
}

function filterByCategory(category) {

  if (category === "") {
    filterProducts = [...products];
  } 
  else {
    filterProducts = products.filter(product =>
      product.category === category
    );
  }

  currentPage = 1;

 renderAll();
}
categoryFilter.addEventListener("change", (e) => {
  filterByCategory(e.target.value);
});

const saveBtn = document.getElementById("saveProductBtn");

saveBtn.addEventListener("click", () => {

  const name = document.getElementById("productName").value.trim();
  const price = Number(document.getElementById("productPrice").value);
  const stock = Number(document.getElementById("productStock").value);
  const category = document.getElementById("productCategory").value;

  if (!name || !price || !stock || !category) {
    alert("Please fill all fields");
    return;
  }

  // if editMode is true, we need to update the existing product instead of adding a new one
  if (editMode) {

    const index = products.findIndex(p => p.id === editId);

    if (index !== -1) {
      products[index] = {
        id: editId,
        name,
        price,
        stock,
        category
      };
    }

    editMode = false;
    editId = null;

  } else {

    // if not in edit mode, we create a new product and add it to the products array
    const newProduct = {
      id: Date.now(),
      name,
      price,
      stock,
      category
    };

    products.push(newProduct);
  }

  // update filterProducts to reflect the changes in products array. This is important because if we are currently filtering or searching, we want the new/edited product to be included in the current view if it matches the criteria.
  filterProducts = [...products];

  saveToStorage();
  renderProducts(filterProducts);
  updateAnalytics(filterProducts);
  loadCategories();

  // reset form fields
  document.getElementById("productName").value = "";
  document.getElementById("productPrice").value = "";
  document.getElementById("productStock").value = "";
  document.getElementById("productCategory").value = "";

  modal.style.display = "none";
});
let editMode = false;
let editId = null;

function editProduct(id) {

  const product = products.find(p => p.id === id);

  if (!product) return;

  // open modal
  modal.style.display = "flex";

  // edit product details in form
  document.getElementById("productName").value = product.name;
  document.getElementById("productPrice").value = product.price;
  document.getElementById("productStock").value = product.stock;
  document.getElementById("productCategory").value = product.category;

  // set edit mode
  editMode = true;
  editId = id;
}

closeModalBtn.addEventListener("click", () => {
  modal.style.display = "none";

  editMode = false;
  editId = null;
});

window.addEventListener("load", () => {
  loadFromStorage();
   loadCategories(); 

  filterProducts = [...products];

   renderAll();
});