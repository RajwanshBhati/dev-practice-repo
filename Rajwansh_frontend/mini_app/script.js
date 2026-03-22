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

function saveToStorage() {
  localStorage.setItem("products", JSON.stringify(products));
}

function loadFromStorage() {
  const data = localStorage.getItem("products");
  if (data) {
    products = JSON.parse(data);
  }
}

const grid = document.getElementById("productsGrid");
const loading = document.getElementById("loading");

const totalProductsEl = document.getElementById("totalProducts");
const totalValueEl = document.getElementById("totalValue");
const outOfStockEl = document.getElementById("outOfStock");
const searchInput = document.getElementById("searchInput");


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
      <button onclick="deleteProduct(${product.id})">Delete</button>
    `;

    grid.appendChild(card);
  });
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
  renderProducts(products);
  updateAnalytics(products); 
}

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

  renderProducts(filterProducts);
  updateAnalytics(filterProducts);
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

  renderProducts(filterProducts);
  updateAnalytics(filterProducts);
}
searchInput.addEventListener("input", (e) => {
  searchProducts(e.target.value);
});

window.addEventListener("load", () => {
  loadFromStorage();
  renderProducts(products);

   updateAnalytics(products);
});