// Mess Management System - Frontend JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.3s';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });

    // Form validation
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;

            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.style.borderColor = '#e74c3c';
                } else {
                    field.style.borderColor = '#ddd';
                }
            });

            if (!isValid) {
                e.preventDefault();
                alert('Please fill in all required fields');
            }
        });
    });

    // Confirm delete actions
    const deleteLinks = document.querySelectorAll('a[href*="/delete/"]');
    deleteLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            const confirmed = confirm('Are you sure you want to delete this item? This action cannot be undone.');
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });

    // Select/Deselect all checkboxes
    const selectAllCheckbox = document.querySelector('#selectAll');
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            const checkboxes = document.querySelectorAll('.member-checkbox');
            checkboxes.forEach(cb => {
                cb.checked = this.checked;
            });
        });
    }

    // Number formatting
    const numberInputs = document.querySelectorAll('input[type="number"]');
    numberInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && this.step === '0.01') {
                this.value = parseFloat(this.value).toFixed(2);
            }
        });
    });

    // Dynamic form fields based on expense type
    const expenseTypeSelect = document.querySelector('select[name="expenseType"]');
    if (expenseTypeSelect) {
        expenseTypeSelect.addEventListener('change', function() {
            const isSharedCheckbox = document.querySelector('input[name="isShared"]');
            if (this.value === 'individual') {
                if (isSharedCheckbox) {
                    isSharedCheckbox.checked = false;
                    isSharedCheckbox.disabled = true;
                }
            } else {
                if (isSharedCheckbox) {
                    isSharedCheckbox.disabled = false;
                }
            }
        });
    }

    // Meal attendance - select all present
    const selectAllPresent = document.querySelector('#selectAllPresent');
    if (selectAllPresent) {
        selectAllPresent.addEventListener('click', function() {
            const presentCheckboxes = document.querySelectorAll('input[name="presentMembers"]');
            presentCheckboxes.forEach(cb => cb.checked = true);
        });
    }

    // Clear all attendance
    const clearAllAttendance = document.querySelector('#clearAllAttendance');
    if (clearAllAttendance) {
        clearAllAttendance.addEventListener('click', function() {
            const presentCheckboxes = document.querySelectorAll('input[name="presentMembers"]');
            presentCheckboxes.forEach(cb => cb.checked = false);
        });
    }

    // Print functionality
    const printButtons = document.querySelectorAll('.btn-print');
    printButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            window.print();
        });
    });

    // Export to CSV (basic implementation)
    const exportButtons = document.querySelectorAll('.btn-export');
    exportButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            const table = document.querySelector('.table');
            if (table) {
                let csv = [];
                const rows = table.querySelectorAll('tr');

                rows.forEach(row => {
                    const cols = row.querySelectorAll('td, th');
                    const rowData = [];
                    cols.forEach(col => {
                        rowData.push('"' + col.textContent.trim() + '"');
                    });
                    csv.push(rowData.join(','));
                });

                const csvContent = csv.join('\n');
                const blob = new Blob([csvContent], { type: 'text/csv' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'export.csv';
                a.click();
                window.URL.revokeObjectURL(url);
            }
        });
    });

    // Real-time calculation for meal costs
    const costInput = document.querySelector('input[name="cost"]');
    const attendeeCountDisplay = document.querySelector('#attendeeCount');
    const perPersonCostDisplay = document.querySelector('#perPersonCost');

    if (costInput && attendeeCountDisplay) {
        function updateMealCost() {
            const cost = parseFloat(costInput.value) || 0;
            const attendeeCount = parseInt(attendeeCountDisplay.textContent) || 1;
            const perPersonCost = (cost / attendeeCount).toFixed(2);
            if (perPersonCostDisplay) {
                perPersonCostDisplay.textContent = perPersonCost + ' ৳';
            }
        }

        costInput.addEventListener('input', updateMealCost);

        const attendanceCheckboxes = document.querySelectorAll('input[name="presentMembers"]');
        attendanceCheckboxes.forEach(cb => {
            cb.addEventListener('change', function() {
                const checkedCount = document.querySelectorAll('input[name="presentMembers"]:checked').length;
                attendeeCountDisplay.textContent = checkedCount;
                updateMealCost();
            });
        });
    }

    // Date validation - prevent future dates where needed
    const dateInputs = document.querySelectorAll('input[type="date"].no-future');
    const today = new Date().toISOString().split('T')[0];
    dateInputs.forEach(input => {
        input.max = today;
    });

    // Phone number formatting (Bangladesh)
    const phoneInputs = document.querySelectorAll('input[type="tel"]');
    phoneInputs.forEach(input => {
        input.addEventListener('input', function() {
            // Remove non-numeric characters
            this.value = this.value.replace(/\D/g, '');

            // Limit to 11 digits for Bangladesh
            if (this.value.length > 11) {
                this.value = this.value.slice(0, 11);
            }
        });
    });

    // Smooth scroll to top
    const scrollTopBtn = document.createElement('button');
    scrollTopBtn.innerHTML = '↑';
    scrollTopBtn.className = 'scroll-top-btn';
    scrollTopBtn.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: #3498db;
        color: white;
        border: none;
        cursor: pointer;
        display: none;
        font-size: 24px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.2);
        z-index: 1000;
    `;

    document.body.appendChild(scrollTopBtn);

    window.addEventListener('scroll', function() {
        if (window.pageYOffset > 300) {
            scrollTopBtn.style.display = 'block';
        } else {
            scrollTopBtn.style.display = 'none';
        }
    });

    scrollTopBtn.addEventListener('click', function() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
});

// Helper function to format currency
function formatCurrency(amount) {
    return parseFloat(amount).toLocaleString('en-BD', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }) + ' ৳';
}

// Helper function to format date
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
    });
}