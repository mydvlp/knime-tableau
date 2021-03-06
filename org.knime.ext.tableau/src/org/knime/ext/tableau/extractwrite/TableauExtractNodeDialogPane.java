/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Feb 5, 2016 (wiswedel): created
 */
package org.knime.ext.tableau.extractwrite;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable.Type;
import org.knime.core.util.FileUtil;
import org.knime.ext.tableau.extractwrite.TableauExtractSettings.FileOverwritePolicy;

/**
 * Dialog for TDE Writer
 *
 * @author Bernd Wiswedel, KNIME AG, Zurich, Switzerland
 * @author Benjamin Wilhelm, KNIME GmbH, Konstanz, Germany
 */
public final class TableauExtractNodeDialogPane extends NodeDialogPane {

    private final FilesHistoryPanel m_filePanel;

    private final JRadioButton m_overwritePolicyAbortButton;

    private final JRadioButton m_overwritePolicyAppendButton;

    private final JRadioButton m_overwritePolicyOverwriteButton;

    private final String[] m_fileExtensions;

    /**
     * Creates a new Dialog for a Tableau extract writer.
     *
     * @param historyId the file panel history id
     * @param fileExtensions the allowed file extensions for the extract file
     */
    public TableauExtractNodeDialogPane(final String historyId, final String... fileExtensions) {
        m_fileExtensions = fileExtensions;
        m_filePanel =
            new FilesHistoryPanel(createFlowVariableModel(TableauExtractSettings.CFG_OUTPUT_LOCATION, Type.STRING),
                historyId, LocationValidation.FileOutput, fileExtensions);
        m_filePanel.setDialogTypeSaveWithExtension(fileExtensions[0]);

        // Overwrite policy buttons
        m_overwritePolicyAppendButton = new JRadioButton("Append");
        m_overwritePolicyOverwriteButton = new JRadioButton("Overwrite");
        m_overwritePolicyAbortButton = new JRadioButton("Abort");
        final ButtonGroup bg = new ButtonGroup();
        bg.add(m_overwritePolicyAppendButton);
        bg.add(m_overwritePolicyOverwriteButton);
        bg.add(m_overwritePolicyAbortButton);

        addTab("Extract Settings", initPanel());
    }

    private JPanel initPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;

        gbc.anchor = GridBagConstraints.EAST;

        p.add(new JLabel("Output Location "), gbcLabel(gbc));
        gbc.gridx += 1;
        p.add(m_filePanel, gbcComponent(gbc));
        gbc.gridy += 1;

        gbc.gridx = 1;
        p.add(new JLabel(" If file exists... "), gbc);
        gbc.gridy += 1;

        // Overwrite policy settings (copied from csv writer)
        final JPanel overwriteFilePane = new JPanel();
        overwriteFilePane.setLayout(new BoxLayout(overwriteFilePane, BoxLayout.X_AXIS));
        m_overwritePolicyOverwriteButton.setAlignmentY(Component.TOP_ALIGNMENT);
        overwriteFilePane.add(m_overwritePolicyOverwriteButton);
        overwriteFilePane.add(Box.createHorizontalStrut(20));
        m_overwritePolicyAppendButton.setAlignmentY(Component.TOP_ALIGNMENT);
        overwriteFilePane.add(m_overwritePolicyAppendButton);
        overwriteFilePane.add(Box.createHorizontalStrut(20));
        m_overwritePolicyAbortButton.setAlignmentY(Component.TOP_ALIGNMENT);
        overwriteFilePane.add(m_overwritePolicyAbortButton);
        overwriteFilePane.add(Box.createHorizontalGlue());

        p.add(overwriteFilePane, gbc);

        m_overwritePolicyAbortButton.doClick();
        return p;
    }

    private static GridBagConstraints gbcLabel(final GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        return gbc;
    }

    private static GridBagConstraints gbcComponent(final GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
        throws NotConfigurableException {
        final TableauExtractSettings s;
        try {
            s = new TableauExtractSettings().loadSettings(settings);
        } catch (InvalidSettingsException e) {
            throw new NotConfigurableException(e.getMessage(), e);
        }
        m_filePanel.updateHistory();
        m_filePanel.setSelectedFile(s.getOutputLocation());
        switch (s.getFileOverwritePolicy()) {
            case Append:
                m_overwritePolicyAppendButton.doClick();
                break;
            case Overwrite:
                m_overwritePolicyOverwriteButton.doClick();
                break;
            case Abort:
                m_overwritePolicyAbortButton.doClick();
                break;
        }
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        final TableauExtractSettings s = new TableauExtractSettings();

        // Get the selected file
        final String selectedFile = m_filePanel.getSelectedFile();

        // Check if the file path ends with one of the given valid extensions
        if (Arrays.stream(m_fileExtensions).noneMatch(e -> selectedFile.endsWith(e))) {
            if (m_fileExtensions.length == 1) {
                throw new InvalidSettingsException(
                    "The file must end with the extension '" + m_fileExtensions[0] + "'.");
            } else {
                throw new InvalidSettingsException(
                    "The file must end with one of the extensions [" + String.join(",", m_fileExtensions) + "].");
            }
        }

        // Check if the file is a local file
        final File file;
        try {
            file = FileUtil.getFileFromURL(FileUtil.toURL(selectedFile));
        } catch (final InvalidPathException | MalformedURLException e) {
            throw new InvalidSettingsException(e);
        }
        if (file == null) {
            throw new InvalidSettingsException("Only local files are allowed.");
        }

        s.setOutputLocation(selectedFile);

        // Save the overwrite policy
        if (m_overwritePolicyAppendButton.isSelected()) {
            s.setFileOverwritePolicy(FileOverwritePolicy.Append);
        } else if (m_overwritePolicyOverwriteButton.isSelected()) {
            s.setFileOverwritePolicy(FileOverwritePolicy.Overwrite);
        } else {
            s.setFileOverwritePolicy(FileOverwritePolicy.Abort);
        }
        s.saveSettings(settings);
    }

}
