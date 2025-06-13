package com.projet.testing.vehicule;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
public class ReportManager {

    private static final String REPORT_DIR = "target/extent-reports/";
    private static final List<String> reportFiles = new ArrayList<>();

    public static ExtentReports createReport(String suiteName) {
        String reportFile = suiteName + "-report.html";
        String reportPath = REPORT_DIR + reportFile;

        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setDocumentTitle(suiteName + " Report");
        reporter.config().setReportName("Résultats - " + suiteName);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);

        reportFiles.add(reportFile);
        return extent;
    }

    public static void generateIndexHtml() {
        try {
            Files.createDirectories(Paths.get(REPORT_DIR));
            Path indexPath = Paths.get(REPORT_DIR, "index.html");
            try (BufferedWriter writer = Files.newBufferedWriter(indexPath)) {
                writer.write("""
                        <!DOCTYPE html>
                        <html lang="fr">
                        <head>
                            <meta charset="UTF-8">
                            <title>Index des Rapports de Test</title>
                            <style>
                                body {
                                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                    background: #f5f5f5;
                                    margin: 0;
                                    padding: 2em;
                                    color: #333;
                                }
                                h1 {
                                    text-align: center;
                                    color: #2c3e50;
                                }
                                ul {
                                    list-style: none;
                                    padding: 0;
                                    max-width: 600px;
                                    margin: 2em auto;
                                }
                                li {
                                    margin: 1em 0;
                                    background: white;
                                    border-left: 4px solid #3498db;
                                    padding: 1em;
                                    transition: background 0.3s;
                                }
                                li:hover {
                                    background: #ecf0f1;
                                }
                                a {
                                    text-decoration: none;
                                    color: #3498db;
                                    font-weight: bold;
                                    display: block;
                                }
                            </style>
                        </head>
                        <body>
                            <h1>Index des Rapports de Test</h1>
                            <ul>
                        """);

                for (String file : reportFiles) {
                    writer.write("<li><a href=\"" + file + "\" target=\"_self\">" + file + "</a></li>\n");
                }

                writer.write("""
                            </ul>
                        </body>
                        </html>
                        """);
            }

            System.out.println("✔ index.html généré avec design dans : " + indexPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

