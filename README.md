# Kun.uz News Portal Backend

O'zbekistonning eng yaxshi yangilik portali - Kun.uz ning backend qismi.

## Loyiha Haqida

Bu loyiha **Spring Boot 3.1.5** va **PostgreSQL** da yozilgan.

### Asosiy Xususiyatlar

1. **Foydalanuvchi Boshqaruvi (Profile Management)**
   - Ro'yhatdan o'tish (Email orqali)
   - Login/Logout
   - Profil ma'lumotlarini yangilash
   - Rol-based access control (RBAC)

2. **Maqola Boshqaruvi (Article Management)**
   - Maqola yozish (Moderator)
   - Maqolani chop etish (Publisher)
   - Maqolani bloklash
   - Ko'rishlar soni, ulushlar

3. **Viloyatlar, Kategoriyalar, Bo'limlar (Region, Category, Section)**
   - CRUD operatsiyalar
   - Multi-language support (UZ, RU, EN)

4. **Kommentariyalar (Comments)**
   - Kommentariya yozish
   - Kommentariyaga javob berish
   - Like/Dislike

5. **Saqlangan Maqolalar (Saved Articles)**
   - Maqolani saqlash
   - Saqlangan maqolalarni ko'rish

## Texnologiyalar

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Security**
- **JWT Token**
- **Flyway** (Database Migration)
- **Lombok**
- **Maven**

## Loyiha Strukturasi

```
src/main/java/uz/kun/
├── entity/          # Entity classlar
│   └── enums/       # Enum classlar
├── repository/      # Repository interfacelar
├── service/         # Service classlar (Business Logic)
├── controller/      # REST Controllers
├── dto/             # Data Transfer Objects
├── config/          # Configuration classlar
├── exception/       # Exception handling
└── KunUzApplication.java

src/main/resources/
├── application.yml              # Spring Boot configuration
└── db/migration/                # Flyway SQL migrations
```

## Boshlash

### Talablar

- Java 17+
- PostgreSQL 12+
- Maven 3.6+

### O'rnatish

1. Repository klonlash
```bash
git clone https://github.com/sardornurmatov/Kun_uz.git
cd Kun_uz
```

2. Database yaratish
```sql
CREATE DATABASE kun_uz;
```

3. Loyihani ishga tushirish
```bash
mvn clean install
mvn spring-boot:run
```

4. Loyiha `http://localhost:8080/api/v1` da ishga tushadi

## Database Schema

Barcha jadvallar avtomatik ravishda **Flyway** tomonidan yaratiladi.

### Asosiy Jadvallar

- `profile` - Foydalanuvchi profillar
- `profile_role` - Foydalanuvchi rollari
- `region` - Viloyatlar
- `category` - Kategoriyalar
- `section` - Bo'limlar
- `article` - Maqolalar
- `article_category` - Maqola-Kategoriya munosabati
- `article_section` - Maqola-Bo'lim munosabati
- `article_like` - Maqolaga like
- `comment` - Kommentariyalar
- `comment_like` - Kommentariyaga like
- `saved_article` - Saqlangan maqolalar
- `email_history` - Email tarixi
- `sms_history` - SMS tarixi
- `tag` - Teglar

## API Endpoints

### Authentication
- `POST /auth/register` - Ro'yhatdan o'tish
- `POST /auth/login` - Tizimga kirish
- `POST /auth/refresh` - Token yangilash

### Profile
- `GET /profile/{id}` - Profil ma'lumotlarini olish
- `PUT /profile/{id}` - Profil ma'lumotlarini yangilash
- `GET /profile/list` - Barcha profillar
- `DELETE /profile/{id}` - Profilni o'chirish

### Region
- `GET /region` - Barcha viloyatlar
- `POST /region` - Viloyat qo'shish (ADMIN)
- `PUT /region/{id}` - Viloyatni yangilash (ADMIN)
- `DELETE /region/{id}` - Viloyatni o'chirish (ADMIN)

### Category
- `GET /category` - Barcha kategoriyalar
- `POST /category` - Kategoriya qo'shish (ADMIN)
- `PUT /category/{id}` - Kategoriyani yangilash (ADMIN)
- `DELETE /category/{id}` - Kategoriyani o'chirish (ADMIN)

### Section
- `GET /section` - Barcha bo'limlar
- `POST /section` - Bo'lim qo'shish (ADMIN)
- `PUT /section/{id}` - Bo'limni yangilash (ADMIN)
- `DELETE /section/{id}` - Bo'limni o'chirish (ADMIN)

### Article
- `GET /article` - Barcha maqolalar
- `GET /article/{id}` - Maqola ma'lumotlari
- `POST /article` - Maqola yozish (MODERATOR)
- `PUT /article/{id}` - Maqolani yangilash (MODERATOR)
- `DELETE /article/{id}` - Maqolani o'chirish (MODERATOR)
- `PUT /article/{id}/publish` - Maqolani chop etish (PUBLISHER)
- `PUT /article/{id}/block` - Maqolani bloklash (PUBLISHER)

## Environment Variables

`application.yml` faylida quyidagi sozlamalarni o'zgartiring:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kun_uz
    username: postgres
    password: postgres

jwt:
  secret: sizning-jwt-secret-key
  expiration: 86400000  # 24 soat
```

## Litsenziya

Bu loyiha MIT litsenziyasi ostida chiqarilgan.

## Muallif

**Sardor Nurmatov**
- GitHub: [@sardornurmatov](https://github.com/sardornurmatov)
- Email: snurmatov735@gmail.com

---

**Kun.uz** - O'zbekistonning eng yaxshi yangilik manba! 📰
