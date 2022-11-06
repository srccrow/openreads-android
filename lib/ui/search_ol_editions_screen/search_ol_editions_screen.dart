import 'package:flutter/material.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';
import 'package:loading_animation_widget/loading_animation_widget.dart';
import 'package:openreads/model/book.dart';
import 'package:openreads/model/ol_edition_result.dart';
import 'package:openreads/resources/open_library_service.dart';
import 'package:openreads/ui/add_book_screen/add_book_screen.dart';
import 'package:openreads/ui/search_ol_editions_screen/widgets/widgets.dart';

class SearchOLEditionsScreen extends StatefulWidget {
  const SearchOLEditionsScreen({
    super.key,
    required this.editions,
    required this.title,
    required this.author,
    required this.pagesMedian,
    required this.isbn,
    required this.olid,
    required this.firstPublishYear,
  });

  final List<String> editions;
  final String title;
  final String author;
  final int? pagesMedian;
  final List<String>? isbn;
  final String? olid;
  final int? firstPublishYear;

  @override
  State<SearchOLEditionsScreen> createState() => _SearchOLEditionsScreenState();
}

class _SearchOLEditionsScreenState extends State<SearchOLEditionsScreen> {
  final sizeOfPage = 6;
  int skippedEditions = 0;

  late int filteredResultsLength;

  final _pagingController = PagingController<int, OLEditionResult>(
    firstPageKey: 0,
    invisibleItemsThreshold: 6,
  );

  Future<void> _fetchPage(int pageKey) async {
    try {
      final newResults = await _fetchResults(offset: pageKey);

      if (!mounted) return;

      if (pageKey == widget.editions.length) {
        _pagingController.appendLastPage(newResults);
      } else {
        final nextPageKey = pageKey + 6;
        _pagingController.appendPage(newResults, nextPageKey);
      }
    } catch (error) {
      if (!mounted) return;
      _pagingController.error = error;
    }
  }

  Future<List<OLEditionResult>> _fetchResults({required int offset}) async {
    final results = List<OLEditionResult>.empty(growable: true);

    for (var i = 0; i < sizeOfPage; i++) {
      bool getEdition = true;

      while (getEdition) {
        final newResult = await OpenLibraryService().getEdition(
          widget.editions[offset + i + skippedEditions],
        );

        if (newResult.covers != null && newResult.covers!.isNotEmpty) {
          results.add(newResult);
          getEdition = false;
        } else {
          skippedEditions += 1;
        }
      }
    }

    return results;
  }

  void _saveEdition({
    required double statusBarHeight,
    required OLEditionResult result,
    required int cover,
  }) {
    final book = Book(
      title: result.title!,
      author: widget.author,
      pages: result.numberOfPages,
      status: 0,
      favourite: false,
      isbn: (result.isbn13 != null && result.isbn13!.isNotEmpty)
          ? result.isbn13![0]
          : (result.isbn10 != null && result.isbn10!.isNotEmpty)
              ? result.isbn10![0]
              : null,
      olid: (result.key != null) ? result.key!.replaceAll('/books/', '') : null,
      publicationYear: widget.firstPublishYear,
    );

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) => AddBook(
        topPadding: statusBarHeight,
        previousThemeData: Theme.of(context),
        fromOpenLibrary: true,
        cover: cover,
        book: book,
      ),
    );
  }

  void _saveNoEdition({
    required double statusBarHeight,
  }) {
    final book = Book(
      title: widget.title,
      author: widget.author,
      status: 0,
      favourite: false,
      pages: widget.pagesMedian,
      isbn: (widget.isbn != null && widget.isbn!.isNotEmpty)
          ? widget.isbn![0]
          : null,
      olid:
          (widget.olid != null) ? widget.olid!.replaceAll('/works/', '') : null,
      publicationYear: widget.firstPublishYear,
    );

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) => AddBook(
        topPadding: statusBarHeight,
        previousThemeData: Theme.of(context),
        fromOpenLibrary: true,
        book: book,
      ),
    );
  }

  @override
  void initState() {
    filteredResultsLength = widget.editions.length;

    if (widget.editions.isNotEmpty) {
      _pagingController.addPageRequestListener((pageKey) {
        _fetchPage(pageKey);
      });
    }

    super.initState();
  }

  @override
  void dispose() {
    _pagingController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final statusBarHeight = MediaQuery.of(context).padding.top;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Choose edition'),
        backgroundColor: Theme.of(context).scaffoldBackgroundColor,
        scrolledUnderElevation: 0,
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Expanded(
            child: Column(
              children: [
                Expanded(
                  child: Scrollbar(
                    child: Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: PagedGridView(
                        pagingController: _pagingController,
                        showNewPageProgressIndicatorAsGridChild: false,
                        builderDelegate:
                            PagedChildBuilderDelegate<OLEditionResult>(
                          firstPageProgressIndicatorBuilder: (_) => Center(
                            child: LoadingAnimationWidget.staggeredDotsWave(
                              color: Theme.of(context).primaryColor,
                              size: 50,
                            ),
                          ),
                          newPageProgressIndicatorBuilder: (_) => Center(
                            child: Padding(
                              padding: const EdgeInsets.all(20.0),
                              child: LoadingAnimationWidget.staggeredDotsWave(
                                color: Theme.of(context).primaryColor,
                                size: 50,
                              ),
                            ),
                          ),
                          itemBuilder: (context, item, index) =>
                              BookCardEdition(
                            title: item.title!,
                            cover: item.covers![0],
                            onPressed: () => _saveEdition(
                              statusBarHeight: statusBarHeight,
                              result: item,
                              cover: item.covers![0],
                            ),
                          ),
                        ),
                        gridDelegate:
                            const SliverGridDelegateWithFixedCrossAxisCount(
                          childAspectRatio: 5.2 / 8.0,
                          crossAxisCount: 2,
                          crossAxisSpacing: 10,
                          mainAxisSpacing: 10,
                          mainAxisExtent: 225,
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
          NoEditionsButton(
            onPressed: () => _saveNoEdition(
              statusBarHeight: statusBarHeight,
            ),
          ),
        ],
      ),
    );
  }
}
