
# 🎓 Academic Prediction System

A web-based application that utilizes an Artificial Neural Network (ANN) to predict students’ academic performance in higher education — designed to empower educators with real-time, data-driven insights for early intervention.

## 📌 Table of Contents

- [Overview](#overview)
- [Why This Project Matters](#why-this-project-matters)
- [Key Features](#key-features)
- [System Architecture](#system-architecture)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)

---

## 🔍 Overview

The **Academic Prediction System** helps identify at-risk students by analyzing various academic and behavioral data using a feedforward Artificial Neural Network (ANN). This tool is especially designed for educators with minimal technical background, enabling them to receive performance predictions and act promptly to support student success.

## 🚀 Why This Project Matters

Many educational institutions face challenges in identifying underperforming students before it's too late. This system addresses that gap by:

- Predicting student outcomes using historical data.
- Providing an accessible platform for non-technical educators.
- Encouraging proactive support to improve student retention and achievement.
- Aligning with **Sustainable Development Goal 4 (SDG 4)**: Inclusive and quality education.

## ✨ Key Features

- Web-based platform with intuitive educator dashboard.
- Predicts academic performance (e.g., CGPA) using ANN.
- Real-time risk assessment and performance insights.
- Secure login and role-based access (Educator/Admin).
- Visualized prediction history for better tracking.
- Data preprocessing, model training & evaluation using MAE.

## 🧱 System Architecture

- **Frontend:** HTML, CSS, JavaScript
- **Backend:** Java (Spring Boot)
- **ML Model:** Python (TensorFlow/Keras)
- **Database:** MySQL
- **Web Server:** Apache Tomcat

## 🛠️ Tech Stack

| Component        | Technology              |
|------------------|--------------------------|
| Frontend         | HTML, CSS, JavaScript    |
| Backend          | Java + Spring Boot       |
| ML Model         | Python, TensorFlow/Keras |
| Database         | MySQL                    |
| Hosting          | Apache Tomcat            |
| Development      | Agile methodology        |

## 📦 Installation

### Prerequisites

- Python 3.10+
- Java JDK 17+
- Node.js (optional, for frontend bundling)
- MySQL Server
- Apache Tomcat

### Backend & ML Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/academic-prediction-system.git
   cd academic-prediction-system
   ```

2. Install Python dependencies:
   ```bash
   cd model
   pip install -r requirements.txt
   ```

3. Train or load the ANN model:
   ```bash
   python train_model.py
   ```

4. Run the Spring Boot backend:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

5. Deploy on Tomcat or localhost as preferred.

### Frontend

Frontend can be directly served as static files or linked to the backend endpoints for predictions.

## 🧪 Usage

1. Educators log in via the web interface.
2. Enter student data (grades, attendance, preparation hours).
3. System displays predicted CGPA or risk level in real-time.
4. Educators can view past predictions and track academic trends.

## 🙋‍♀️ Contributing

Contributions are welcome! Here's how you can help:

- Open an [Issue](https://github.com/yourusername/academic-prediction-system/issues) for bugs or feature requests.
- Fork the repository and submit a pull request.
- Improve documentation, UI, or refactor code.

Please read `CONTRIBUTING.md` before starting.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgements

- **Assoc. Prof. Dr. Nurul Hashimah Ahamed Hassain Malim** – Supervisor
- **Dr. Zarul Fitri Zaaba** – Examiner
- **School of Computer Sciences, Universiti Sains Malaysia**
- All academic researchers and contributors referenced in the report.
- Dataset from **Mendeley Data** (Student Performance Metrics)

---

> Developed by Fatiha Atiera Binti Mohd Haris | Universiti Sains Malaysia (2024/2025)
