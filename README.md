# EdIE-R
**<em>A tool for phenotyping brain imaging reports.</em>**

The Edinburgh Information Extraction for Radiology reports (EdIE-R) system is a text mining pipeline designed to classify reports of CT and MRI brain scans. It automatically assigns phenotypes, labels that indicate the presence and type of conditions such as types of stroke and tumour, along with other neurological findings. EdIE-R extracts entities, detects negation and identifies relationships between entities within each report, and generates report-level labels.

Developed through extensive analysis and validation using Scottish brain imaging reports and expert-annotated data, EdIE-R was originally developed for and evaluated on datasets from the [Edinburgh Stroke Study](https://doi.org/10.1186/s13326-019-0211-7) and [NHS Tayside](https://doi.org/10.1186/s12911-019-0908-7).

The version (EdIE-R-2026) released here has been externally tested on brain imaging reports from [Generation Scotland](https://genscot.ed.ac.uk) reported in our paper (Alex et al., 2026).

Documentation specific to the tool itself, how to run it, its input and output etc. can be found in the `doc` folder.

## Supported Phenotypes

EdIE-R labels reports with the following 24 phenotypes at the document-level:

#### Stroke (11)
Ischaemic stroke: Deep recent/old · Cortical recent/old  · Underspecified
Haemorrhagic stroke: Deep/Lobar recent/old · Underspecified 
Stroke: underspecified

#### Tumour (4)
Meningioma · Metastasis · Glioma · Other

#### Other Neurological Findings (9)
Small vessel disease
Atrophy
Subdural haematoma
Subarachnoid haemorrhage: aneurysmal/other
Microbleed: Deep · Lobar · Underspecified
Haemorrhagic transformation

## Installation

```bash
git clone https://github.com/Edinburgh-LTG/EdIE-R
```

## Citation

Alex, Beatrice, Grover, Claire, Casey, Arlene, Tobin, Richard, Whalley, Heather, & Whiteley, William (2026). GS-BrainText: A Multi-Site Brain Imaging Report Dataset from Generation Scotland for Clinical Natural Language Processing Development and Validation, In Proceedings of the Clinical NLP Workshop, LREC 2026, ELRA Language Resources Association, Palma, Spain, 16th of May 2026, arXiv preprint arXiv:2603.26235.

```
@inproceedings{alex2026gs-braintext,
  title={GS-BrainText: A Multi-Site Brain Imaging Report Dataset from Generation Scotland for Clinical Natural Language Processing Development and Validation},
  author={Alex, Beatrice and Grover, Claire and Casey, Arlene and Tobin, Richard and Whalley, Heather and Whiteley, William},
  booktitle={LREC 2026},
  year={2026},
  organization={ELRA Language Resources Association},
  location={Palma, Spain},
  date={16th of May 2026},
  url={https://arxiv.org/abs/2603.26235}
}
```

## Contact

If you have questions about the tool, please contact [Beatrice Alex](https://www.hw.ac.uk/profiles/uk/school/macs/faculty/beatrice-alex).
