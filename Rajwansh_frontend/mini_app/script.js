// Sample product data
let products = [
  { id: 1, name: "Laptop", price: 55000, stock: 5, category: "electronics" },
  {
    id: 2,
    name: "Smartphone",
    price: 20000,
    stock: 10,
    category: "electronics",
  },
  { id: 3, name: "Headphones", price: 2000, stock: 2, category: "accessories" },
  {
    id: 4,
    name: "Smart Watch",
    price: 5000,
    stock: 0,
    category: "electronics",
  },
  {
    id: 5,
    name: "Gaming Mouse",
    price: 1500,
    stock: 12,
    category: "accessories",
  },
  { id: 6, name: "Keyboard", price: 2500, stock: 4, category: "electronics" },
  { id: 7, name: "T-Shirt", price: 800, stock: 15, category: "clothing" },
  { id: 8, name: "Jeans", price: 1500, stock: 0, category: "clothing" },
  { id: 9, name: "Jacket", price: 3000, stock: 6, category: "clothing" },
  {
    id: 10,
    name: "Book - JavaScript Guide",
    price: 500,
    stock: 8,
    category: "books",
  },
  {
    id: 11,
    name: "Book - HTML & CSS",
    price: 400,
    stock: 3,
    category: "books",
  },
  { id: 12, name: "Backpack", price: 1200, stock: 3, category: "accessories" },
  { id: 13, name: "Tablet", price: 30000, stock: 4, category: "electronics" },
  { id: 14, name: "Shoes", price: 2200, stock: 7, category: "clothing" },
];

// Filtered products list used for search, sort, filters
let filterProducts = [...products];

// Pagination state is used because it track current page
let currentPage = 1;
const itemsPerPage = 8;

// Save products data to browser storage
function saveToStorage() {
  localStorage.setItem("products", JSON.stringify(products));
}

// Load products data from browser storage if used, otherwise use default products array
function loadFromStorage() {
  const data = localStorage.getItem("products");
  if (data) {
    products = JSON.parse(data);
  }
}

// create common function call these function whenever we need to re-render the page after any action
function renderAll() {
  const paginatedData = paginateData(filterProducts, currentPage);

  renderProducts(paginatedData);
  renderPagination(filterProducts);
  updateAnalytics(filterProducts);
}

// Dom elements
const grid = document.getElementById("productsGrid");
const loading = document.getElementById("loading");

// Analytics elements to show total products, total inventory value and out of stock products

const totalProductsEl = document.getElementById("totalProducts");
const totalValueEl = document.getElementById("totalValue");
const outOfStockEl = document.getElementById("outOfStock");

// Input elements
const searchInput = document.getElementById("searchInput");
const categoryFilter = document.getElementById("categoryFilter");
const lowStockFilter = document.getElementById("lowStockFilter");

// Render products on UI
function renderProducts(list) {
  grid.innerHTML = "";

  // if list is empty show no products found message
  if (list.length === 0) {
    grid.innerHTML = "<p>No products found</p>";
    return;
  }

  // otherwise render products
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

    // append card to grid
    grid.appendChild(card);
  });
}

// Function to get paginated data based on current page and items per page
function paginateData(data, page) {
  const start = (page - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return data.slice(start, end);
}

// Function to render pagination buttons based on total items and current page
function renderPagination(data) {
  const pagination = document.getElementById("pagination");
  pagination.innerHTML = "";

  // Calculate total pages based on data length and items per page
  const totalPages = Math.ceil(data.length / itemsPerPage);

  // Prev button to go to previous page, disabled on first page
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

  // Page number buttons
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

  // Next button to go to next page, disabled on last page
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

// Update dashboard status total products, value, out of stock
function updateAnalytics(list) {
  // Total Products should be the length of the products array
  totalProductsEl.textContent = list.length;

  // Total Inventory Value (price × stock)
  const totalValue = list.reduce((sum, product) => {
    return sum + product.price * product.stock;
  }, 0);

  totalValueEl.textContent = totalValue;

  //  Out of Stock Products
  const outOfStock = list.filter((product) => product.stock === 0).length;

  outOfStockEl.textContent = outOfStock;
}

// Delete product by id
function deleteProduct(id) {
  // remove from products array
  products = products.filter((p) => p.id !== id);

  // update filtered array
  filterProducts = [...products];

  currentPage = 1;

  saveToStorage();

  // re-render everything properly
  renderAll();
}

// Modal elements
const modal = document.getElementById("productModal");
const addBtn = document.getElementById("addProductBtn");
const closeModalBtn = document.getElementById("closeModalBtn");

// Open modal on add button click
addBtn.addEventListener("click", () => {
  modal.style.display = "flex";
});

// Close modal
closeModalBtn.addEventListener("click", () => {
  modal.style.display = "none";
});

// Sorting products
function sortProductPrice(type) {
  if (type === "price-low") {
    filterProducts.sort((a, b) => a.price - b.price);
  } else if (type === "price-high") {
    filterProducts.sort((a, b) => b.price - a.price);
  } else if (type === "Atoz") {
    filterProducts.sort((a, b) => a.name.localeCompare(b.name));
  } else if (type === "ZtoA") {
    filterProducts.sort((a, b) => b.name.localeCompare(a.name));
  }

  currentPage = 1;

  renderAll();
}
const sorts = document.getElementById("sortFilter");

sorts.addEventListener("change", (e) => {
  sortProductPrice(e.target.value);
});

// Apply filters based on search input, category and low stock
function applyFilters() {
  let filtered = [...products];

  if (searchInput.value) {
    filtered = filtered.filter((p) =>
      p.name.toLowerCase().includes(searchInput.value.toLowerCase()),
    );
  }

  if (categoryFilter.value) {
    filtered = filtered.filter((p) => p.category === categoryFilter.value);
  }

  if (lowStockFilter.value === "low") {
    filtered = filtered.filter((p) => p.stock < 5);
  }

  filterProducts = filtered;

  currentPage = 1;
  renderAll();
}

// Add event listeners to search input, category filter and low stock filter to apply filters whenever any of these inputs change
lowStockFilter.addEventListener("change", applyFilters);

// Search products by name
function searchProducts(query) {
  query = query.toLowerCase().trim();

  if (query === "") {
    filterProducts = [...products];
  } else {
    filterProducts = products.filter((product) =>
      product.name.toLowerCase().includes(query),
    );
  }

  currentPage = 1;

  renderAll();
}
searchInput.addEventListener("input", (e) => {
  searchProducts(e.target.value);
});

// Load categories dynamically in category filter dropdown based on products data
function loadCategories() {
  categoryFilter.innerHTML = `<option value="">All Categories</option>`;

  const categories = [...new Set(products.map((p) => p.category))];

  categories.forEach((cat) => {
    const option = document.createElement("option");
    option.value = cat;
    option.textContent = cat.charAt(0).toUpperCase() + cat.slice(1);
    categoryFilter.appendChild(option);
  });
}

// Filter products by category
function filterByCategory(category) {
  if (category === "") {
    filterProducts = [...products];
  } else {
    filterProducts = products.filter(
      (product) => product.category === category,
    );
  }

  currentPage = 1;

  renderAll();
}
categoryFilter.addEventListener("change", (e) => {
  filterByCategory(e.target.value);
});
// Variables to track whether we are in edit mode and which product is being edited. This is important because when we click on edit button, we want to populate the form with existing product details and when we save, we want to update the existing product instead of adding a new one.
let editMode = false;
let editId = null;
// Save product (both add and edit)
const saveBtn = document.getElementById("saveProductBtn");

saveBtn.addEventListener("click", () => {
  const name = document.getElementById("productName").value.trim();
  const price = Number(document.getElementById("productPrice").value);
  const stock = Number(document.getElementById("productStock").value);
  const category = document.getElementById("productCategory").value;

  // Basic validation for product details before saving
  if (name === "") {
    showToast("Product name cannot be empty", "error");
    return;
  }

  if (isNaN(price) || price <= 0) {
    showToast("Price must be greater than 0", "error");
    return;
  }

  if (isNaN(stock) || stock < 0) {
    showToast("Stock cannot be negative", "error");
    return;
  }

  if (!category) {
    showToast("Category must be selected", "error");
    return;
  }

  // if editMode is true, we need to update the existing product instead of adding a new one
  if (editMode) {
    const index = products.findIndex((p) => p.id === editId);

    if (index !== -1) {
      products[index] = {
        id: editId,
        name,
        price,
        stock,
        category,
      };
    }

    editMode = false;
    editId = null;
    showToast("Product updated successfully", "success");
  } else {
    // if not in edit mode, we create a new product and add it to the products array
    const newProduct = {
      id: Date.now(),
      name,
      price,
      stock,
      category,
    };

    products.push(newProduct);
    showToast("Product added successfully", "success");

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

// Edit product by id
function editProduct(id) {
  const product = products.find((p) => p.id === id);

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

// Function to show toast messages for better user experience
function showToast(message, type = "info") {
  const container = document.getElementById("toastContainer");

  const toast = document.createElement("div");
  toast.classList.add("toast", type);
  toast.innerText = message;

  container.appendChild(toast);

  // auto remove after 3 sec
  setTimeout(() => {
    toast.remove();
  }, 3000);
}

window.addEventListener("load", () => {
  // Showing loader while we load data and render the page for better UX
  loading.style.display = "flex";

  //disable filters while loading
  searchInput.disabled = true;
  categoryFilter.disabled = true;
  sorts.disabled = true;
  addBtn.disabled = true;

  setTimeout(() => {
    // laod data from localStorage if available, otherwise use default products array
    loadFromStorage();
    loadCategories();

    filterProducts = [...products];

    renderAll();

    // hide loader after rendering is done
    loading.style.display = "none";

    // enable filters after loading and rendering is done
    searchInput.disabled = false;
    categoryFilter.disabled = false;
    sorts.disabled = false;
    addBtn.disabled = false;
  }, 1000);
});
