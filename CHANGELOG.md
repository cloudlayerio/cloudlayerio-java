# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-03-17

### Added

- Initial release of the cloudlayer.io Java SDK
- **Conversion API**: 10 conversion methods — URL/HTML/Template to PDF/Image, DOCX to PDF/HTML, PDF to DOCX, Merge PDFs
- **Data Management API**: Jobs, Assets, Storage, Account, Status, Templates
- **Utility Methods**: `waitForJob()` with configurable polling, `downloadJobResult()` for v2 workflow
- **v1/v2 Support**: v1 returns raw binary, v2 returns Job objects with async processing
- **Builder Pattern**: Immutable option classes with fluent builders for all endpoints
- **Exception Hierarchy**: `CloudLayerException` → `ApiException`/`AuthException`/`RateLimitException`/`NetworkException`/`TimeoutException`/`ConfigException`/`ValidationException`
- **Retry Logic**: Exponential backoff with jitter for retryable requests (429, 5xx)
- **Client-side Validation**: Fast feedback for common input errors
- **Jackson Serialization**: Custom serializers for union types (`StorageOption`, `GeneratePreviewOption`, `NullableString`, `LayoutDimension`)
- **Thread Safety**: `CloudLayer` client is immutable and safe for concurrent use
- **File Upload**: Multipart support for DOCX/PDF document conversion endpoints
- **Base64 Helper**: `HtmlUtil.encodeHtml()` for HTML conversion endpoints

### Requirements

- Java 11+
- Runtime dependency: Jackson (JSON serialization)
