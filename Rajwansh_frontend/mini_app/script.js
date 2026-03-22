let products = [
  { id: 1, name: "Laptop", price: 55000, stock: 5, category: "electronics" },
  { id: 2, name: "Smartphone", price: 20000, stock: 10, category: "electronics" },
  { id: 3, name: "Headphones", price: 2000, stock: 2, category: "accessories" },
  { id: 4, name: "T-Shirt", price: 800, stock: 15, category: "clothing" },
  { id: 5, name: "Jeans", price: 1500, stock: 0, category: "clothing" },
  { id: 6, name: "Book - JS Guide", price: 500, stock: 8, category: "books" },
  { id: 7, name: "Backpack", price: 1200, stock: 3, category: "accessories" },
  { id: 8, name: "Tablet", price: 30000, stock: 4, category: "electronics" }
];


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


window.addEventListener("load", () => {
  loadFromStorage();
  renderProducts(products);
});