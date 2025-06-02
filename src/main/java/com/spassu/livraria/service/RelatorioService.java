package com.spassu.livraria.service;

import com.spassu.livraria.exception.RelatorioDatabaseException;
import com.spassu.livraria.exception.RelatorioErroException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypeBuilders;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final DataSource dataSource;

    public byte[] gerarRelatorioLivrosPorAutor() {
        try (Connection conn = dataSource.getConnection()) {
            JasperReportBuilder report = criarRelatorio(conn);
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            report.toPdf(pdfStream);
            return pdfStream.toByteArray();
        } catch (DRException | RuntimeException e) {
            log.error("Erro de geração de relatório: {}", e.getMessage());
            throw new RelatorioErroException();
        } catch (Exception ex) {
            log.error("Erro: {}", ex.getMessage());
            throw new RelatorioDatabaseException();
        }
    }

    private JasperReportBuilder criarRelatorio(Connection conn) {
        DataTypeBuilders type = new DataTypeBuilders();

        ValueColumnBuilder<?, String> colunaGrupo = Columns.column("Autor", "autor_nome", type.stringType());
        ColumnGroupBuilder grupo = grp.group(colunaGrupo)
                .setStyle(stl.style().bold())
                .footer(Components.line().setPen(stl.pen1Point().setLineColor(Color.LIGHT_GRAY)));

        StyleBuilder cabecalhoStyle = criarEstiloCabecalho();

        return report()
                .columns(
                        Columns.column("Título", "titulo", type.stringType()),
                        Columns.column("Editora", "editora", type.stringType()),
                        Columns.column("Edição", "edicao", type.stringType()),
                        Columns.column("Ano", "ano_publicacao", type.stringType()),
                        Columns.column("Valor", "valor", type.stringType()),
                        Columns.column("Assuntos", "assuntos", type.stringType())
                )
                .groupBy(grupo)
                .title(
                        Components.verticalGap(40),
                        Components.text("Livros agrupados por Autor")
                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setStyle(stl.style().bold().setFontSize(14)),
                        Components.verticalGap(5),
                        Components.text("Gerado em: " +
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setStyle(stl.style().italic().setFontSize(9)),
                        Components.verticalGap(50)
                )
                .setColumnTitleStyle(cabecalhoStyle)
                .setDetailStyle(stl.style().setPadding(4))
                .highlightDetailEvenRows()
                .pageFooter(
                        Components.pageXofY().setStyle(stl.style().setFontSize(8))
                )
                .setDataSource("SELECT * FROM vw_livros_autores_assuntos", conn);
    }

    private StyleBuilder criarEstiloCabecalho() {
        return stl.style()
                .setBackgroundColor(Color.LIGHT_GRAY)
                .setBold(true);
    }
}
