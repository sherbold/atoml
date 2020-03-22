import dash
import dash_table
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output

import pandas as pd
from sqlalchemy import create_engine

db_connection_str = 'mysql+pymysql://atoml:atoml@127.0.0.1:3306/atoml'

db_connection = create_engine(db_connection_str)

frameworks = ['Weka', 'sklearn', 'Spark']

# create app
external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']
app = dash.Dash(__name__, external_stylesheets=external_stylesheets)

app.config.suppress_callback_exceptions = True

app.layout = html.Div([
    dcc.Location(id='url', refresh=False),
    html.Div(id='page-content')
])

# create start page
start_page = html.Div(children=[
    html.H1(children='Atoml Dashboard'),

    html.Div(children='''
            This Dashboard shows gives an overview of the test results achieved with Atoml, a framework for the automated testing of machine learning libraries. 
            The goal of Atoml is to provide a set of smoke tests that ensure that implementations of learning algorithms are functioning on a basic level. 
            For classification algorithms, this means that they must be able to train a model and perform predictions without crashing.
        '''),
    dcc.Link('Results for Weka', href='/weka'),
    html.Br(),
    dcc.Link('Results for sklearn', href='/sklearn'),
    html.Br(),
    dcc.Link('Results for Spark', href='/spark')

])

pages = {}

for framework in frameworks:
    # create general overview page
    query = """
    SELECT passed, count(passed)
    FROM smoketest_view
    WHERE framework='%s'
    GROUP BY passed
    ORDER BY passed""" % framework.lower()
    df_smoke_failed = pd.read_sql(query, con=db_connection)

    if len(df_smoke_failed)<2:
        if df_smoke_failed.at[0, 'passed']==1:
            smoke_passed = df_smoke_failed.at[0,'count(passed)']
            smoke_failed = 0
        else:
            smoke_failed = df_smoke_failed.at[0, 'count(passed)']
            smoke_passed = 0
    else:
        smoke_passed = df_smoke_failed.iat[1,1]
        smoke_failed = df_smoke_failed.iat[0,1]

    query = """
    SELECT count(*) as total,
           count(*)-sum(passed_exact_score) as failed_exact_score,
           count(*)-sum(passed_exact_class) as failed_exact_class,
           count(*)-sum(passed_stat_class) as failed_stat_class,
           count(*)-sum(passed_stat_score) as failed_stat_score
    FROM morphtest_view
    WHERE framework='%s'""" % framework.lower()
    df_metamorphic_overview = pd.read_sql(query, con=db_connection)

    pages[framework.lower()] = html.Div(children=[
        html.H1(children='Atoml Test Results for %s' % framework),

        html.Div(children='''
            Dashboard for the tests results computed with atoml.
        '''),

        dcc.Link('Go to the smoke test results', href='/%s/smoketests' % framework.lower()),
        html.Br(),
        dcc.Link('Go to the metamorphic test results', href='/%s/morphtests' % framework.lower()),
        html.Br(),
        dcc.Link('Go back to home', href='/'),

        html.H2(children='Overview of Results for all Classifiers'),

        dcc.Graph(
            id='smoke-tests-total-%s' % framework,
            figure={
                'data': [
                    {'x': ['Passed'], 'y': [smoke_passed], 'type': 'bar', 'name': 'Passed'},
                    {'x': ['Errored'], 'y': [smoke_failed], 'type': 'bar', 'name': 'Errored'},
                ],
                'layout': {
                    'title': 'Total results for smoke tests of of all classifiers'
                }
            }
        ),

        dcc.Graph(
            id='morph-tests-total-%s' % framework,
            figure={
                'data': [
                    {'x': ['Executed'], 'y': [df_metamorphic_overview.at[0,'total']], 'type': 'bar', 'name': 'Executed'},
                    {'x': ['Not exact scores'], 'y': [df_metamorphic_overview.at[0,'failed_exact_score']], 'type': 'bar', 'name': 'Not exact scores'},
                    {'x': ['Not exact classification'], 'y': [df_metamorphic_overview.at[0,'failed_exact_class']], 'type': 'bar', 'name': 'Not exact classification'},
                    {'x': ['Statistically significantly different classification'], 'y': [df_metamorphic_overview.at[0,'failed_stat_class']], 'type': 'bar', 'name': 'Statistically significantly different classification'},
                    {'x': ['Statistically significantly different scores'], 'y': [df_metamorphic_overview.at[0,'failed_stat_score']], 'type': 'bar', 'name': 'Statistically significantly different scores'},
                ],
                'layout': {
                    'title': 'Total results for morph tests of of all classifiers'
                }
            }
        ),
    ])

    # create smoke test overview page
    smoke_grouped = pd.read_sql("SELECT * FROM smoketest_view WHERE passed=0 and framework='%s' GROUP BY testcase, `algorithm`, parameters, exception" % framework.lower(), con=db_connection)
    smoke_grouped['parameters'] = smoke_grouped['parameters'].str.replace(",'", ", '")

    pages['%s/smoketests'%framework.lower()] = html.Div([
        html.H1(children='Overview of Smoke Tests Failures'),
        html.Div(children='List of crashes found with smoke tests. Enable the checkboxes to see full parameter list and stacktrace. '),
        dash_table.DataTable(
            id='smoke-results-%s' % framework,
            style_cell={
                'overflow': 'hidden',
                'textOverflow': 'ellipsis',
                'maxWidth': 0,
                'textAlign': 'left',
            },
            style_cell_conditional=[
                {'if': {'column_id': 'testcase'},
                 'width': '130px'},
                {'if': {'column_id': 'algorithm'},
                 'width': '200px'},
            ],
            sort_action="native",
            sort_mode="multi",
            column_selectable="multi",
            columns=[{"name": 'Testcase', "id": 'testcase'},
                     {'name': 'Algorithm', 'id': 'algorithm'},
                     {'name': 'Parameters', 'id': 'parameters', 'selectable': True},
                     {'name': 'Stacktrace', 'id': 'stacktrace', 'selectable': True},],
            data=smoke_grouped.to_dict('records'),
        ),
        dcc.Link('Go back to overview for %s' % framework, href='/%s' % framework.lower()),
        html.Br(),
        dcc.Link('Go back to home', href='/')
    ])

    @app.callback(Output('smoke-results-%s' % framework, 'style_data_conditional'),
                  [Input('smoke-results-%s' % framework, 'selected_columns')])
    def update_styles(selected_columns):
        if selected_columns is not None:
            return [{
                'if': {'column_id': i},
                'whiteSpace': 'normal',
                'height': 'auto',
            } for i in selected_columns]

    # Create morph test overview page
    algorithms = pd.read_sql("SELECT DISTINCT algorithm FROM morphtest_view WHERE framework='%s'" % framework.lower(), con=db_connection)['algorithm'].tolist()

    cur_children = []
    for alg in algorithms:
        query = """
        SELECT testcase,
               count(*),
               count(*)-sum(passed_exact_score) as ex_sc,
               count(*)-sum(passed_exact_class) as ex_cl,
               count(*)-sum(passed_stat_class) as st_cl,
               count(*)-sum(passed_stat_score) as st_sc
        FROM morphtest_view
        WHERE framework='%s' AND algorithm='%s'
        GROUP BY testcase
        ORDER BY testcase
        """ % (framework.lower(), alg)
        test_result = pd.read_sql(query, con=db_connection)

        title = html.H3(children='Results for %s' % alg)
        link_to_details = dcc.Link('Go to detailed results', href='/%s/morphtests/%s' % (framework.lower(), alg.lower()))
        tbl = dash_table.DataTable(
            id='morph-results-table-%s-%s'%(framework,alg),
            style_cell={
                'overflow': 'hidden',
                'textOverflow': 'ellipsis',
                'maxWidth': 0,
            },
            style_data_conditional=[
                {
                    'if': {
                        'column_id': 'ex_sc',
                        'filter_query': '{ex_sc} > 0'
                    },
                    'backgroundColor': '#f51b00',
                    'color': 'white',
                },{
                    'if': {
                        'column_id': 'ex_cl',
                        'filter_query': '{ex_cl} > 0'
                    },
                    'backgroundColor': '#f51b00',
                    'color': 'white',
                },{
                    'if': {
                        'column_id': 'st_cl',
                        'filter_query': '{st_cl} > 0'
                    },
                    'backgroundColor': '#f51b00',
                    'color': 'white',
                },{
                    'if': {
                        'column_id': 'st_sc',
                        'filter_query': '{st_sc} > 0'
                    },
                    'backgroundColor': '#f51b00',
                    'color': 'white',
                }
            ],
            column_selectable="multi",
            columns=[{"name": 'Testcase', "id": 'testcase'},
                     {'name': 'Not exact scores', 'id': 'ex_sc'},
                     {'name': 'Not exact classes', 'id': 'ex_cl'},
                     {'name': 'Statistically significantly different classification', 'id': 'st_cl'},
                     {'name': 'Statistically significantly different scores', 'id': 'st_sc'}, ],
            data=test_result.to_dict('records')
        )
        cur_children.append(title)
        cur_children.append(link_to_details)
        cur_children.append(tbl)

    pages['%s/morphtests' % framework.lower()] = html.Div([
        html.H1(children='Results of Metamorphic Tests per Algorithm'),
        html.Div(children=cur_children),
        dcc.Link('Go back to overview for %s' % framework, href='/%s' % framework.lower()),
        html.Br(),
        dcc.Link('Go back to home', href='/')
    ])

    # details for morph test results of a single classifier
    testcases = pd.read_sql("SELECT DISTINCT testcase FROM morphtest_view WHERE framework='%s'" % framework.lower(), con=db_connection)['testcase'].tolist()

    morph_alg_detail_pages = {}
    for alg in algorithms:
        cur_children = []
        for testcase in testcases:
            query = """
            SELECT parameters, dataset,
                   count(*),
                   count(*)-sum(passed_exact_score) as ex_sc,
                   count(*)-sum(passed_exact_class) as ex_cl,
                   count(*)-sum(passed_stat_class) as st_cl,
                   count(*)-sum(passed_stat_score) as st_sc
            FROM morphtest_view
            WHERE framework='%s' AND algorithm='%s' AND testcase='%s'
            GROUP BY parameters, dataset
            ORDER BY parameters, dataset
            """ % (framework.lower(), alg, testcase)
            test_result = pd.read_sql(query, con=db_connection)

            title = html.H2(children='Results for %s' % testcase)
            tbl = dash_table.DataTable(
                id='morph-results-table-%s-%s'%(alg, testcase),
                style_cell={
                    'overflow': 'hidden',
                    'textOverflow': 'ellipsis',
                    'maxWidth': 0,
                },
                style_data_conditional=[
                    {
                        'if': {
                            'column_id': 'ex_sc',
                            'filter_query': '{ex_sc} > 0'
                        },
                        'backgroundColor': '#f51b00',
                        'color': 'white',
                    },{
                        'if': {
                            'column_id': 'ex_cl',
                            'filter_query': '{ex_cl} > 0'
                        },
                        'backgroundColor': '#f51b00',
                        'color': 'white',
                    },{
                        'if': {
                            'column_id': 'st_cl',
                            'filter_query': '{st_cl} > 0'
                        },
                        'backgroundColor': '#f51b00',
                        'color': 'white',
                    },{
                        'if': {
                            'column_id': 'st_sc',
                            'filter_query': '{st_sc} > 0'
                        },
                        'backgroundColor': '#f51b00',
                        'color': 'white',
                    }
                ],
                sort_action="native",
                sort_mode="multi",
                column_selectable="multi",
                columns=[{"name": 'Parameters', "id": 'parameters'},
                         {"name": 'Dataset', "id": 'dataset'},
                         {'name': 'Not exact scores', 'id': 'ex_sc'},
                         {'name': 'Not exact classes', 'id': 'ex_cl'},
                         {'name': 'Statistically significantly different classification', 'id': 'st_cl'},
                         {'name': 'Statistically significantly different scores', 'id': 'st_sc'}, ],
                data=test_result.to_dict('records')
            )
            cur_children.append(title)
            cur_children.append(tbl)

        pages['%s/morphtests/%s' % (framework.lower(), alg.lower())] = html.Div([
            html.H1(children='Results of Metamorphic Tests for %s' % alg),
            html.Div(children=cur_children),
            dcc.Link('Go back to overview of metamorphic tests', href='/%s/morphtests' % framework.lower()),
            html.Br(),
            dcc.Link('Go back to overview for %s' % framework, href='/%s' % framework.lower()),
            html.Br(),
            dcc.Link('Go back to home', href='/')
        ])


# Update the index
@app.callback(dash.dependencies.Output('page-content', 'children'),
              [dash.dependencies.Input('url', 'pathname')])
def display_page(pathname):
    if pathname is None:
        return start_page
#    for key in pages.keys():
#        print(key)

    # normalize pathname
    pathname = pathname.lower()
    if pathname.endswith('/'):
        pathname = pathname[:-1]
    if pathname.startswith('/'):
        pathname = pathname[1:]

    if pathname in pages:
        return pages[pathname]
    else:
        return start_page
    # You could also return a 404 "URL not found" page here

if __name__ == '__main__':
    app.run_server(debug=True)